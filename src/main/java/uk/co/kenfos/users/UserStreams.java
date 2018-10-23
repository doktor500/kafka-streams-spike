package uk.co.kenfos.users;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import uk.co.kenfos.json.JsonDeserializer;
import uk.co.kenfos.json.JsonSerializer;
import uk.co.kenfos.model.User;

import static org.apache.kafka.common.serialization.Serdes.String;
import static org.apache.kafka.common.serialization.Serdes.serdeFrom;
import static uk.co.kenfos.users.UsersStreamConfig.USERS_CREATED;
import static uk.co.kenfos.users.UsersStreamConfig.USERS_DELETED;

public class UserStreams {

    private KStream<String, User> usersCreated;
    private KTable<String, User> usersDeleted;

    public UserStreams(StreamsBuilder builder) {
        usersCreated = builder.stream(USERS_CREATED, Consumed.with(String(), userSerde()));
        usersDeleted = builder.table(USERS_DELETED, Consumed.with(String(), userSerde()));
    }

    public Serde<User> userSerde() {
        JsonSerializer<User> serializer = new JsonSerializer<>();
        JsonDeserializer<User> deserializer = new JsonDeserializer<>(User.class);
        return serdeFrom(serializer, deserializer);
    }

    public KStream<String, User> getUsersCreated() {
        return usersCreated;
    }

    public KTable<String, User> getUsersDeleted() {
        return usersDeleted;
    }
}
