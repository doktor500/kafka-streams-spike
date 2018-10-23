#!/usr/bin/env bash

id=$(( ( RANDOM % 100000 )  + 1 ))

user="${id}:{\"id\": ${id}, \"name\": \"${id}\"}"

echo ${user} | kafka-console-producer --broker-list localhost:9092 --property parse.key=true --property key.separator=: --topic users-created > /dev/null
