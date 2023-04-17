import os
import yaml
import boto3
import argparse
import time
from botocore.config import Config
from subprocess import PIPE, Popen


def create_green():
    aws_config = Config(
            region_name=os.environ.get('AWS_ECR_REGION'),
            signature_version='v4',
            retries={
                'max_attempts': 10,
                'mode': 'standard'
                }
            )
    ecr_client = boto3.client('ecr', config=aws_config)
    with open("aline-financial/values.yaml") as yaml_in_stream:
        values_yaml = yaml.safe_load(yaml_in_stream)
        for i in range(len(values_yaml.get('microservices'))):
            ecr_images = ecr_client.describe_images(repositoryName=values_yaml.get('microservices')[i].get('name')).get('imageDetails')
            if not ecr_images:
                continue

            most_recent_tag = list(sorted(ecr_images, key=lambda image: image.get('imagePushedAt'), reverse=True))[0].get('imageTags')[0]
            if values_yaml.get('microservices')[i]['tag'] == most_recent_tag:
                continue
            values_yaml.get('microservices')[i]['green_tag'] = most_recent_tag
    with open("aline-financial/values.yaml", "w") as yaml_out_stream:
        yaml.dump(values_yaml, yaml_out_stream, default_flow_style=False, sort_keys=False)

    command = run_helm()
    if command[0] == 1:
        print(f'ERROR: {command[1]}')
    else:
        print('Created green')



def rollout_green():
    with open("aline-financial/values.yaml") as yaml_in_stream:
        values_yaml = yaml.safe_load(yaml_in_stream)
        for i in range(len(values_yaml.get('microservices'))):
            if not values_yaml['microservices'][i].get('green_tag'):
                continue
            values_yaml.get('microservices')[i]['tag'] = values_yaml.get('microservices')[i].get('green_tag')
            del values_yaml['microservices'][i]['green_tag']

    with open("aline-financial/values.yaml", "w") as yaml_out_stream:
        yaml.dump(values_yaml, yaml_out_stream, default_flow_style=False, sort_keys=False)
    command = run_helm()
    if command[0] == 1:
        print(f'ERROR: {command[1]}')
    else:
        print('Rolled out green')


def rollout_canary(wait_time_seconds):
    if not wait_time_seconds.isdigit():
        return 1, "time not is seconds"
    wait_time_seconds = int(wait_time_seconds)
    with open("aline-financial/values.yaml") as yaml_in_stream:
        values_yaml = yaml.safe_load(yaml_in_stream)
    update_required = []
    for i in range(len(values_yaml.get('microservices'))):
        if not values_yaml['microservices'][i].get('green_tag'):
            continue
        update_required.append(i)

    for i in range(1, 10):
        for m in update_required:
            command = run_helm(microservice=m, weight=i*10)
            if command[0] == 1:
                print(f'ERROR: {command[1]}')
                return
            else:
                print(f'Shifted {i*10}% to green')
        time.sleep(wait_time_seconds)

    rollout_green()


def rollback_green():
    with open("aline-financial/values.yaml") as yaml_in_stream:
        values_yaml = yaml.safe_load(yaml_in_stream)
        for i in range(len(values_yaml.get('microservices'))):
            del values_yaml['microservices'][i]['green_tag']

    with open("aline-financial/values.yaml", "w") as yaml_out_stream:
        yaml.dump(values_yaml, yaml_out_stream, default_flow_style=False, sort_keys=False)
    command = run_helm()
    if command[0] == 1:
        print(f'ERROR: {command[1]}')
        return
    else:
        print('Rolled back green')


def run_helm(microservice=None, weight=None):
    helm_upgrade_cmd = [
        'helm', 'upgrade', '-n', 'microservices',
        'aline-financial', './aline-financial',
    ]
    if weight:
        helm_upgrade_cmd.append('--set')
        helm_upgrade_cmd.append(f'green_weight={weight}')
        helm_upgrade_cmd.append('--set')
        helm_upgrade_cmd.append(f'weight={100-weight}')

    process = Popen(helm_upgrade_cmd, stdout=PIPE, stderr=PIPE)
    stdout, stderr = process.communicate()
    if stderr:
        return 1, stderr
    return 0, stdout


def main():
    parser = argparse.ArgumentParser(
        description='Which stage of deployment needs to be executed'
    )
    parser.add_argument('function', choices=[
        'create_green', 'rollout_green', 'rollback_green', 'rollout_canary'
        ], help='which action do you want to take')
    parser.add_argument('--time_for_canary', required=False, help='time for each canary update')
    args = parser.parse_args()
    if args.function == 'create_green':
        create_green()
    elif args.function == 'rollout_green':
        rollout_green()
    elif args.function == 'rollback_green':
        rollback_green()
    elif args.function == 'rollout_canary':
        rollout_canary(args.time_for_canary)


if __name__ == '__main__':
    main()
