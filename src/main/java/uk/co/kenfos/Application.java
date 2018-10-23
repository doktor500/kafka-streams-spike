package uk.co.kenfos;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.co.kenfos.users.ActiveUsersKStream;
import uk.co.kenfos.users.CreatedUsersCountWindowedKTable;
import uk.co.kenfos.users.CreatedUsersStore;
import uk.co.kenfos.users.UserTopics;

import static uk.co.kenfos.users.UsersStreamConfig.properties;

@SpringBootApplication
public class Application {
    public static void main(String [] args) {
        SpringApplication.run(Application.class, args);

        StreamsBuilder builder = new StreamsBuilder();
        UserTopics userTopics = new UserTopics(builder);

        new ActiveUsersKStream(userTopics).initialize();
        new CreatedUsersCountWindowedKTable(userTopics).initialize().withContinuousQuery();
        CreatedUsersStore createdUsersStore = new CreatedUsersStore(userTopics).initialize();

        KafkaStreams streams = new KafkaStreams(builder.build(), properties());
        streams.start();

        createdUsersStore.query(streams);
    }
}
