#!/usr/bin/env bash

kafka-topics --zookeeper localhost:2181 --delete --topic "users-.*" > /dev/null