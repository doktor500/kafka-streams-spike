package uk.co.kenfos.users;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import uk.co.kenfos.json.JsonDeserializer;
import uk.co.kenfos.json.JsonSerializer;
import uk.co.kenfos.model.User;

import java.util.Objects;

import static org.apache.kafka.common.serialization.Serdes.String;
import static org.apache.kafka.common.serialization.Serdes.serdeFrom;
import static uk.co.kenfos.users.UsersStreamConfig.*;

public class UsersStream {

    public static void main(String[] args) {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, User> usersCreated = builder.stream(USERS_CREATED, Consumed.with(String(), userSerde()));
        KTable<String, User> usersDeleted = builder.table(USERS_DELETED, Consumed.with(String(), userSerde()));

        usersCreated.leftJoin(usersDeleted, UsersStream::joinWhenNotEqual)
            .filter((id, user) -> !Objects.isNull(user))
            .to(USERS_ACTIVE, Produced.with(String(), userSerde()));

        KafkaStreams streams = new KafkaStreams(builder.build(), properties());
        streams.start();
    }

    private static User joinWhenNotEqual(User user1, User user2) {
        return Objects.equals(user1, user2) ? null : user1;
    }

    private static Serde<User> userSerde() {
        JsonSerializer<User> serializer = new JsonSerializer<>();
        JsonDeserializer<User> deserializer = new JsonDeserializer(User.class);
        return serdeFrom(serializer, deserializer);
    }
}
