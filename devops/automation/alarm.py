import boto3
import datetime


def check_ec2(region):
    ec2 = boto3.resource('ec2', region_name=region)
    instances = ec2.instances.filter(
            Filters=[{'Name': 'instance-state-name', 'Values': ['running']}])
   
    for instance in instances:
        ttl_time = 8
        deletable = False
        if instance.tags != None:
            for tag in instance.tags:
                if (tag['Key'] == 'Owner' or tag['Key'] == 'owner') and 'Ilya' in tag['Value']:
                    deletable = True
                elif tag['Key'] == 'TTL' or tag['Key'] == 'ttl':
                    ttl_time = float(tag['Value'])
        now = datetime.datetime.now(datetime.timezone.utc)
        check_time = now - datetime.timedelta(hours=ttl_time)
        if deletable and instance.launch_time < check_time:
            print(f'terminated ec2 instance {instance.id}')
            instance.terminate()


def check_rds(region):
    rds = boto3.client('rds', region_name=region)
    for instance in rds.describe_db_instances()['DBInstances']:
        tags = rds.list_tags_for_resource(ResourceName=instance["DBInstanceArn"])['TagList']
        ttl_time = 8
        deletable = False
        for tag in tags:
            if (tag['Key'] == 'Owner' or tag['Key'] == 'owner') and 'Ilya' in tag['Value']:
                deletable = True
            if tag['Key'] == 'TTL' or tag['Key'] == 'ttl':
                ttl_time = float(tag['Value'])
        now = datetime.datetime.now(datetime.timezone.utc)
        check_time = now - datetime.timedelta(hours=ttl_time)
        if deletable and instance['InstanceCreateTime'] < check_time:
            print(f'terminated rds instance {instance["DBInstanceIdentifier"]}')
            rds.delete_db_instance(DBInstanceIdentifier=instance['DBInstanceIdentifier'], SkipFinalSnapshot=True)


def lambda_handler(event, context):
    regions = ['us-east-1','us-east-2','us-west-1','us-west-2']
    for region in regions:
        check_ec2(region)
        check_rds(region)

