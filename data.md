   
    bin/zookeeper-server-start.sh config/zookeeper.properties
    bin/kafka-server-start.sh config/server.properties
    bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic users-created && bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic users-created
    bin/kafka-console-producer.sh --broker-list localhost:9092 --property parse.key=true --property key.separator=: --topic users-created
    bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic users-deleted && bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic users-deleted
    bin/kafka-console-producer.sh --broker-list localhost:9092 --property parse.key=true --property key.separator=: --topic users-deleted 
    
users created

    1:{"id": 1, "name": "David"}
    2:{"id": 2, "name": "Ni"}
    3:{"id": 3, "name": "Alex"}

users deleted

    2:{"id": 2, "name": "Ni"}
    3:{"id": 3, "name": "Alex"}