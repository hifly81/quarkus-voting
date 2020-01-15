#!/bin/bash
#title           :clean-docker.sh
#description     :This script will delete docker containers and images.
#author		     :hifly81
#date            :20190410
#version         :0.1
#usage		     :bash clean-docker.sh
#notes           :requires: docker, curl
#==============================================================================

DOCKER_HOST=$(ip -4 addr show docker0 | grep -Po 'inet \K[\d.]+')
printf "\nDocker host: ${DOCKER_HOST}"

############################ Docker prune
echo -e "\nDeleting Docker containers and images for Voting...."

docker stop $(docker ps -a | grep voting | cut -d ' ' -f 1)
docker stop $(docker ps -a | grep kafka | cut -d ' ' -f 1)
docker stop $(docker ps -a | grep zookeeper | cut -d ' ' -f 1)
docker stop $(docker ps -a | grep postgres | cut -d ' ' -f 1)
docker rm $(docker ps -a | grep voting | cut -d ' ' -f 1)
docker rm $(docker ps -a | grep kafka | cut -d ' ' -f 1)
docker rm $(docker ps -a | grep zookeeper | cut -d ' ' -f 1)
docker rm $(docker ps -a | grep postgres | cut -d ' ' -f 1)
docker rmi debezium/zookeeper
docker rmi debezium/kafka
docker rmi debezium/postgres
docker rmi voting-service-quarkus


echo -e "\nPruning done"