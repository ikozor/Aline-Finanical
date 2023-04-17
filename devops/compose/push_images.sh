#!/bin/bash

echo Bulding microservice images
docker compose build

microservice=( $(docker images | grep -oh "ik-\w*-\w*") )
export ecr_url=239153380322.dkr.ecr.us-east-1.amazonaws.com
export push_tag=2.0.0

aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin ${ecr_url}

for i in "${microservice[@]}" 
do
    echo "$i"
    docker tag "$i" "${ecr_url}/${i}:${push_tag}"
    docker push "${ecr_url}/${i}:${push_tag}"
done
