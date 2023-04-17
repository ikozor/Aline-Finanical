FROM alpine/k8s:1.23.15

RUN wget https://releases.hashicorp.com/terraform/1.3.6/terraform_1.3.6_linux_amd64.zip
RUN unzip terraform_1.3.6_linux_amd64.zip && rm terraform_1.3.6_linux_amd64.zip
RUN mv terraform /usr/bin/terraform
