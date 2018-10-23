## Kafka streams PoC

USAGE:

        sh src/scripts/users/create-users-topics.sh
        ./gradlew bootRun

TODO:

- Materialize active users to an external data store

NOTES:

- Deletion can be implemented just by adding a new event to a users topic with an empty boy but the purpose of this spike is to test a join operation