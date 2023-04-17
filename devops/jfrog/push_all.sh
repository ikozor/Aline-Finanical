#!/bin/bash

echo Bulding microservice images
docker compose -f ../compose/docker-compose.yml build

microservice=( $(docker images | grep -oh "ik-\w*-\w*") )

for i in "${microservice[@]}" 
do
    echo "$i"
    docker tag "$i" "${JFROG_REGISTRY}/${i}/${i}"
    docker push "${JFROG_REGISTRY}/${i}/${i}"
done
