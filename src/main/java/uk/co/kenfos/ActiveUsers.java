package uk.co.kenfos;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import uk.co.kenfos.json.JsonDeserializer;
import uk.co.kenfos.json.JsonSerializer;
import uk.co.kenfos.model.User;

import java.util.Properties;

public class ActiveUsers {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    private static final String ACTIVE_USERS = "active-users";
    private static final String USERS_CREATED = "users-created";
    private static final String USERS_DELETED = "users-deleted";

    public static void execute() {
        KStreamBuilder builder = new KStreamBuilder();
        KTable<String, User> usersCreated = builder.table(Serdes.String(), userSerd(), USERS_CREATED);
        KTable<String, User> usersDeleted = builder.table(Serdes.String(), userSerd(), USERS_DELETED);
        usersCreated.leftJoin(usersDeleted, ActiveUsers::createUser).print();
        KafkaStreams streams = startStreams(builder);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static final User createUser(User userCreated, User userDeleted) {
        return new User(userCreated.getId(), userCreated.getName(), userDeleted == null);
    }

    private static Serde<User> userSerd() {
        JsonSerializer<User> serializer = new JsonSerializer<>();
        JsonDeserializer<User> deserializer = new JsonDeserializer(User.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    private static KafkaStreams startStreams(KStreamBuilder builder) {
        KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration());
        streams.cleanUp();
        streams.start();
        return streams;
    }

    private static Properties streamsConfiguration() {
        Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, ACTIVE_USERS);
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        streamsConfiguration.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        return streamsConfiguration;
    }
}
