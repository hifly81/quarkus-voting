#!/bin/bash
#title           :deploy-docker.sh
#description     :This script will create the env
#author		     :hifly81
#date            :20190410
#version         :0.1
#usage		     :bash deploy-docker.sh
#notes           :requires: docker, curl
#==============================================================================

DOCKER_HOST=$(ip -4 addr show docker0 | grep -Po 'inet \K[\d.]+')
printf "\nDocker host: ${DOCKER_HOST}"

############################ Docker prune
echo -e "\nDeleting Docker containers running for Voting...."

docker stop $(docker ps -a | grep kafka | cut -d ' ' -f 1)
docker stop $(docker ps -a | grep zookeeper | cut -d ' ' -f 1)
docker stop $(docker ps -a | grep voting | cut -d ' ' -f 1)


echo -e "\nPruning done. Starting application..."

############################ Postgres

echo -e "\nStart Postgresql container...."
docker run -d --name postgres -p 5432:5432 debezium/postgres
sleep 5
echo -e "\nCREATE voting database...."
docker exec -it postgres psql -h localhost -p 5432 -U postgres -c 'CREATE DATABASE voting;'
echo -e "\nPostgresql started."


############################ Zookeeper
sleep 5
echo -e "\nStart Zookeeper container...."
docker run -d --name zookeeper -p 2181:2181 -p 2888:2888 -p 3888:3888 debezium/zookeeper
echo -e "\nZookeeper started."

############################ Kafka
sleep 5
echo -e "\nStart Kafka container...."
docker run -d --name my-cluster-kafka-bootstrap -p 9092:9092 --link zookeeper:zookeeper debezium/kafka
echo -e "\nKafka started."

