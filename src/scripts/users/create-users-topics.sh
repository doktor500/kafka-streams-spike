#!/usr/bin/env bash

kafka-topics --create \
  --zookeeper localhost:2181 \
  --replication-factor 1 \
  --partitions 1 \
  --topic users-created

kafka-topics --create \
  --zookeeper localhost:2181 \
  --replication-factor 1 \
  --partitions 1 \
  --topic users-deleted

cat $(dirname $0)/data/created-users-input.txt | kafka-console-producer --broker-list localhost:9092 --property parse.key=true --property key.separator=: --topic users-created
