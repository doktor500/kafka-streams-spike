package uk.co.kenfos;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.co.kenfos.users.ActiveUsersStreamExample;
import uk.co.kenfos.users.UserStreams;
import uk.co.kenfos.users.UsersStreamWindowExample;

import static uk.co.kenfos.users.UsersStreamConfig.properties;

@SpringBootApplication
public class Application {
    public static void main(String [] args) {
        SpringApplication.run(Application.class, args);

        StreamsBuilder builder = new StreamsBuilder();
        UserStreams userStreams = new UserStreams(builder);

        new ActiveUsersStreamExample(userStreams).main(args);
        new UsersStreamWindowExample(userStreams).main(args);
        new KafkaStreams(builder.build(), properties()).start();
    }
}
