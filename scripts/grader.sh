#!/usr/bin/env bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

# run batch for submission
timeout 400 $DIR/batch_submission.sh

# remove any leftover containers
docker ps -a -q | xargs docker rm -f &> /dev/null
docker container prune -f &> /dev/null