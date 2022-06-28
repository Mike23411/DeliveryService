#!/usr/bin/env bash

exec &>> results.txt

now=$(date +"%m-%d-%y %r")
echo "Start time : $now"

# remove old files
docker rmi -f gatech/streamingwars
# build & run new student
docker build -t gatech/streamingwars -f DockerfileV2 ./ --no-cache --rm

# slightly higher than the grader 370 vs 400
timeout 420 scripts/grader.sh

now=$(date +"%m-%d-%y %r")
echo "Finish time : $now"