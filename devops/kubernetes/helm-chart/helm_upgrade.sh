#!/bin/bash

#docker run -v ${PWD}:/apps --rm -it --entrypoint "bash"  alpine/k8s:1.23.14
#docker run -v ${PWD}:/apps -e AWS_ACCESS_KEY_ID=${AWS_ACCESS_KEY_ID} -e AWS_SECRET_ACCESS_KEY=${AWS_SECRET_ACCESS_KEY} alpine/k8s:1.23.14 bash upgrade_helm.sh

: ${AWS_REGION:='us-east-2'}
: ${HELM_INSTALLATION_NAME:='aline-financial'}
: ${EKS_CLUSTER_NAME:= 'ik-aline-financial'}

export UPGRADE_STATUS="Success"

# Check if proper aws credentials are given
if [[ ! $(aws sts get-caller-identity) ]]; then
	export UPGRADE_STATUS="none/incorrect aws credentials loaded"
	echo ${UPGRADE_STATUS}
	exit 1
fi

# Check if cluster exists
if [[ $(aws eks list-clusters --region ${AWS_REGION} | grep ${EKS_CLUSTER_NAME} | tr -s [:blank:] | tr -d [=\"=]) != ${EKS_CLUSTER_NAME} ]]; then
	export UPGRADE_STATUS="Cluster ${EKS_CLUSTER_NAME} not found"	
	echo ${UPGRADE_STATUS}
	exit 1
fi

# Check if cluster is active
if [[ $(aws eks describe-cluster --region ${AWS_REGION} --name ${EKS_CLUSTER_NAME} | grep 'status' | tr -s [:blank:] | tr -d [=\"=]) != " status: ACTIVE," ]]; then
	export UPGRADE_STATUS="Cluster ${EKS_CLUSTER_NAME} is not active"
	echo ${UPGRADE_STATUS}
	exit 1
fi

aws eks update-kubeconfig --region ${AWS_REGION} --name ${EKS_CLUSTER_NAME}

if [[ $(helm status ${HELM_INSTALLATION_NAME}) ]]; then
	helm upgrade ${HELM_INSTALLATION_NAME} aline-financial 	
else
	helm install ${HELM_INSTALLATION_NAME} aline-financial	
fi
