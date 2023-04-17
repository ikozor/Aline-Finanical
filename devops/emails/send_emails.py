import os
import boto3
from templates import pipeline_result


def send_html_email():
    ses_client = boto3.client("ses", region_name=os.environ.get('AWS_REGION'))
    microservice = os.environ.get('microservice')
    git_commit = os.environ.get('git_commit')
    jenkins_build_number = os.environ.get('build_number')
    job_url = os.environ.get('job_url')
    git_url = os.environ.get('git_url')
    sonarqube_url = os.environ.get('sonarqube_url')
    failure_stage = os.environ.get('failure_stage') if os.environ.get('failure_stage') != 'None' else None
    from_email = os.environ.get('from_email')
    to_emails = str(os.environ.get('to_emails'))
    email_list = to_emails.replace(' ', '').split(',')
    if email_list[-1] == '':
        email_list.pop()

    response = ses_client.send_email(
        Destination={
            "ToAddresses": email_list,
        },
        Message={
            "Body": {
                "Html": {
                    "Charset": "UTF-8",
                    "Data": pipeline_result(
                                            microservice=microservice,
                                            git_commit=git_commit,
                                            jenkins_build_number=jenkins_build_number,
                                            job_url=job_url,
                                            git_url=git_url,
                                            sonarqube_url=sonarqube_url,
                                            failure_stage=failure_stage
                    ),
                }
            },
            "Subject": {
                "Charset": "UTF-8",
                "Data": f"Jenkins {microservice} pipeline {'failed' if failure_stage else 'passed'}",
            },
        },
        Source=from_email,
    )
    print(response)


if __name__ == '__main__':
    send_html_email()
