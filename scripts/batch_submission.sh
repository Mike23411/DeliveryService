#!/usr/bin/env bash

# SCENARIO=$1
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
regex="commands_([0-9]+).txt"

mkdir -p ./docker_results
docker run -d --name dronedelivery gatech/dronedelivery sh -c "mkdir docker_results && sleep 400"

for test in $DIR/../test_scenarios/*;
do
  if [[ $test =~ $regex ]]
    then
        SCENARIO="${BASH_REMATCH[1]}"
        docker exec dronedelivery sh -c "java -jar drone_delivery.jar < commands_${SCENARIO}.txt > drone_delivery_${SCENARIO}_results.txt && \
        tr '[:upper:]' '[:lower:]' < drone_delivery_${SCENARIO}_results.txt > temp_${SCENARIO}.txt && mv temp_${SCENARIO}.txt drone_delivery_${SCENARIO}_results.txt && \
        tr -d '[:blank:]' < drone_delivery_${SCENARIO}_results.txt > temp_${SCENARIO}.txt && mv temp_${SCENARIO}.txt drone_delivery_${SCENARIO}_results.txt && \
        tr '[:upper:]' '[:lower:]' < drone_delivery_initial_${SCENARIO}_results.txt > temp_${SCENARIO}.txt && mv temp_${SCENARIO}.txt drone_delivery_initial_${SCENARIO}_results.txt && \
        tr -d '[:blank:]' < drone_delivery_initial_${SCENARIO}_results.txt > temp_${SCENARIO}.txt && mv temp_${SCENARIO}.txt drone_delivery_initial_${SCENARIO}_results.txt && \
        diff -a -b -E -s -N -u drone_delivery_${SCENARIO}_results.txt drone_delivery_initial_${SCENARIO}_results.txt > docker_results/diff_results_${SCENARIO}.txt" &
    fi
done
wait
# /usr/src/cs6310/
docker cp dronedelivery:/usr/src/cs6310/docker_results/ ./
docker rm -f dronedelivery &> /dev/null