from os import environ
from time import sleep
import yaml
import boto3
import argparse
from botocore.config import Config
from subprocess import PIPE, Popen

MICROSERVICES = [
        "account",
        "bank",
        "card",
        "transaction",
        "underwriter",
        "user"
]
aws_config = Config(
        region_name='us-east-1',
        signature_version='v4',
        retries={
            'max_attempts': 10,
            'mode': 'standard'
            }
        )

ecr_client = boto3.client('ecr', config=aws_config)


def create_green(service):
    if service not in MICROSERVICES:
        raise Exception
    tag = "0.1.0"
    ecr_images = ecr_client.describe_images(
        repositoryName=f'ik-{service}-microservice').get('imageDetails')
    if ecr_images:
        tag = list(sorted(ecr_images,
                          key=lambda image: image.get('imagePushedAt'),
                          reverse=True))[0].get('imageTags')[0]

    port = environ.get(f'GREEN_{service.upper()}_PORT')
    service_name = f'{service}-{tag.replace(".","-")}'
    green_service = {
        'labels': ['green', tag],
        'image': f'${{AWS_ACC_ID}}.dkr.ecr.${{AWS_REGION}}.amazonaws.com/ik-{service}-microservice:{tag}',
        'environment': {
            'ENCRYPT_SECRET_KEY': '${ENCRYPT_SECRET_KEY}',
            'JWT_SECRET_KEY': '${JWT_SECRET_KEY}',
            'DB_USERNAME': '${DB_USERNAME}',
            'DB_PASSWORD': '${DB_PASSWORD}',
            'DB_HOST': '${DB_HOST}',
            'DB_PORT': '${DB_PORT}',
            'DB_NAME': '${DB_NAME}',
            'APP_PORT': port,
        },
        'ports': [f'{port}:{port}']
    }
    with open("docker-compose.yml") as yaml_in_stream:
        compose_file = yaml.safe_load(yaml_in_stream)
        compose_services = compose_file.get('services')
        compose_resources = compose_file['x-aws-cloudformation']['Resources']

    if service_name not in compose_services:
        resource_svc_name = f'{service.capitalize()}{tag.replace(".", "")}'

        compose_services.update({service_name: green_service})
        compose_resources.update({
            f'{resource_svc_name}TCP{port}TargetGroup': {
                'Properties': {
                    'Protocol': 'HTTP',
                    'HealthCheckIntervalSeconds': 60,
                    'UnhealthyThresholdCount': 5,
                    'HealthCheckPath': '/health'
                }
            },
            f'{resource_svc_name}Service': {
                'Properties': {
                    'NetworkConfiguration': {
                        'AwsvpcConfiguration': {
                            'Subnets': ['${AWS_SUBNET_1}', '${AWS_SUBNET_2}']
                        }
                    }
                }
            },
            f'{resource_svc_name}TCP{port}Listener': {
                'Properties': {
                    'Protocol': 'HTTP',
                    'DefaultActions': [{
                        'ForwardConfig': {
                            'TargetGroups': [{
                                'Weight': 1,
                                'TargetGroupArn': {
                                    'Ref': f'{resource_svc_name}TCP{port}TargetGroup'
                                }
                            }]
                        },
                        'Type': 'forward'
                    }]
                }
            }
        })

    with open("docker-compose.yml", "w") as yaml_out_stream:
        yaml.dump(compose_file, yaml_out_stream, default_flow_style=False, sort_keys=False)

    compose_up()


def rollout_green(service):
    with open("docker-compose.yml") as yaml_in_stream:
        compose_file = yaml.safe_load(yaml_in_stream)
        compose_services = compose_file['services']
        compose_resources = compose_file['x-aws-cloudformation']['Resources']

    blue_service, blue_tag, green_service, green_tag = get_services(service, compose_services)
    port = f'${{{service.upper()}_PORT}}'
    compose_services[green_service]['labels'][0] = 'blue'
    compose_services[green_service]['labels'][1] = green_tag
    compose_services[green_service]['environment']['APP_PORT'] = port
    compose_services[green_service]['ports'] = [f'{port}:{port}']

    compose_services[blue_service] = compose_services[green_service]

    resource_svc_name = f'{service.capitalize()}{green_tag.replace(".","")}'
    green_port = environ.get("GREEN_" + service.upper() + "_PORT")

    target_groups = compose_resources[f'{resource_svc_name}TCP{green_port}Listener']['Properties']['DefaultActions'][0]['ForwardConfig']['TargetGroups']
    if len(target_groups) > 1:
        target_groups.pop()
    del compose_services[green_service]
    del compose_resources[f'{resource_svc_name}Service']
    del compose_resources[f'{resource_svc_name}TCP{green_port}TargetGroup']
    del compose_resources[f'{resource_svc_name}TCP{green_port}Listener']
    with open("docker-compose.yml", "w") as yaml_out_stream:
        yaml.dump(compose_file, yaml_out_stream, default_flow_style=False, sort_keys=False)

    print(compose_up()[1])


def rollout_canary(service, shifting_time_seconds):
    with open("docker-compose.yml") as yaml_in_stream:
        compose_file = yaml.safe_load(yaml_in_stream)
        compose_services = compose_file['services']
        compose_resources = compose_file['x-aws-cloudformation']['Resources']

    try:
        blue_service, blue_tag, green_service, green_tag = get_services(service, compose_services)
    except TypeError:
        print('missing blue or green service')
        return

    capitaized_svc_name = service.capitalize()
    resource_svc_name = f'{service.capitalize()}{green_tag.replace(".","")}'
    svc_port = environ.get(f'{service.upper()}_PORT')
    green_svc_port = environ.get(f'GREEN_{service.upper()}_PORT')

    compose_resources[f'{capitaized_svc_name}TCP{svc_port}Listener']\
        ['Properties']['DefaultActions'][0]['ForwardConfig']['TargetGroups']\
        .append({
            'Weight': 0,
            'TargetGroupArn': {
                'Ref': f'{resource_svc_name}TCP{green_svc_port}TargetGroup'}
        })

    for i in range(1, 11):
        compose_resources[f'{capitaized_svc_name}TCP{svc_port}Listener']\
            ['Properties']['DefaultActions'][0]['ForwardConfig']['TargetGroups']\
            [-1].update({'Weight': i})
        compose_resources[f'{capitaized_svc_name}TCP{svc_port}Listener']\
            ['Properties']['DefaultActions'][0]['ForwardConfig']['TargetGroups']\
            [-2].update({'Weight': 10 - i})

        with open("docker-compose.yml", "w") as yaml_out_stream:
            yaml.dump(compose_file, yaml_out_stream, default_flow_style=False, sort_keys=False)
        code, output = compose_up()
        if code == 1:
            print(code, '\n', output)
            print('rolling back')
            del compose_resources[f'{capitaized_svc_name}TCP{svc_port}Listener']\
                ['Properties']['DefaultActions'][0]['ForwardConfig']['TargetGroups'][-1]
            compose_resources[f'{capitaized_svc_name}TCP{svc_port}Listener']\
                ['Properties']['DefaultActions'][0]['ForwardConfig']['TargetGroups']\
                [0].update({'Weight': 10})
            rollback_green(service)
            return
        print(f'Shifted {i*10}% of traffic')
        sleep(shifting_time_seconds)
    print('finishing rollout')

    del compose_resources[f'{capitaized_svc_name}TCP{svc_port}Listener']\
        ['Properties']['DefaultActions'][0]['ForwardConfig']['TargetGroups'][-1]
    compose_resources[f'{capitaized_svc_name}TCP{svc_port}Listener']\
        ['Properties']['DefaultActions'][0]['ForwardConfig']['TargetGroups']\
        [0].update({'Weight': 10})
    rollout_green(service)


def rollback_green(service):

    with open("docker-compose.yml") as yaml_in_stream:
        compose_file = yaml.safe_load(yaml_in_stream)
        compose_services = compose_file['services']
        compose_resources = compose_file['x-aws-cloudformation']['Resources']

    blue_service, blue_tag, green_service, green_tag = get_services(service, compose_services)
    resource_svc_name = f'{service.capitalize()}{green_tag.replace(".","")}'
    green_port = environ.get("GREEN_" + service.upper() + "_PORT")

    del compose_services[green_service]
    del compose_resources[f'{resource_svc_name}Service']
    del compose_resources[f'{resource_svc_name}TCP{green_port}TargetGroup']

    with open("docker-compose.yml", "w") as yaml_out_stream:
        yaml.dump(compose_file, yaml_out_stream, default_flow_style=False, sort_keys=False)

    print(compose_up()[1])


def get_services(service, compose_services):
    service_names = []
    for service_name in compose_services:
        if service in service_name:
            service_names.append(service_name)

    if len(service_names) < 2:
        print("Nothing to do")
        return

    blue_service = ''
    green_service = ''
    for svc in service_names:
        if 'blue' in compose_services.get(svc).get('labels'):
            blue_service = svc
            blue_tag = compose_services.get(svc).get('labels')[1]
        elif 'green' in compose_services.get(svc).get('labels'):
            green_service = svc
            green_tag = compose_services.get(svc).get('labels')[1]
    if green_service == '':
        print('missing green service')
        raise exit
    elif blue_service == '':
        print('missing blue service')
        raise exit
    return blue_service, blue_tag, green_service, green_tag


def compose_up():
    print('running compose up command')
    process = Popen(['docker', 'compose', 'up'], stdout=PIPE, stderr=PIPE)
    code = process.returncode
    stdout, stderr = process.communicate()
    print(code)
    print('Finished Running compose up, waiting for cloudformation stack')
    for i in range(10):
        stack_status = get_stack_status('ecs')
        if stack_status not in (0, 1):
            return 1, stack_status
        if stack_status == 0:
            return code, stdout
        print('cloudformation stack updating, checking status in 10 seconds')
        sleep(10)
    return 1, 'cloudformation stack update taking too long \nexiting'


def get_stack_status(stack_name):
    command = ["aws",
               "cloudformation",
               "describe-stacks",
               "--stack-name",
               stack_name,
               "--query",
               "Stacks[].StackStatus",
               "--output", "text"]
    process = Popen(command, stdout=PIPE, stderr=PIPE)
    stdout, stderr = process.communicate()
    if stderr:
        return stderr
    stdout = str(stdout)
    if '_FAILED' in stdout:
        return 'Failed to update'
    if '_COMPLETE' in stdout:
        return 0
    else:
        return 1


def main():
    parser = argparse.ArgumentParser(description='Which stage of blue/green needs to be executed')
    parser.add_argument('function',
                        choices=['create_green',
                                 'rollout_green',
                                 'rollback_green',
                                 'rollout_canary'],
                        help='Which step to execute')
    parser.add_argument('service', choices=MICROSERVICES, help='Which microservice would you like to deploy')
    args = parser.parse_args()
    if args.function == 'create_green':
        create_green(args.service)
    elif args.function == 'rollout_green':
        rollout_green(args.service)
    elif args.function == 'rollback_green':
        rollback_green(args.service)
    elif args.function == 'rollout_canary':
        rollout_canary(args.service, 10)


if __name__ == '__main__':
    main()
