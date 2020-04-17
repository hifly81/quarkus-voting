#!/bin/bash
#title           :build-images.sh
#description     :This script will create docker images
#author		     :hifly81
#date            :20190410
#version         :0.1
#usage		     :bash build-images.sh
#notes           :requires: mvn, docker
#==============================================================================

image_voting_name=voting
image_voting_version=latest


############################ Voting Service

#create image
cd ../voting/
mvn clean package -Pnative -Dnative-image.docker-build=true
docker build -f Dockerfile.native -t ${image_voting_name}:${image_voting_version} .

