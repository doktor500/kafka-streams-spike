package uk.co.kenfos.users;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import uk.co.kenfos.model.User;

import static org.apache.kafka.streams.state.QueryableStoreTypes.keyValueStore;
import static uk.co.kenfos.users.StreamUtils.waitUntilStoreIsReady;
import static uk.co.kenfos.users.UsersStreamConfig.USERS_STORE;

@Slf4j
@AllArgsConstructor
public class CreatedUsersStore {

    private final UserTopics userTopics;

    public CreatedUsersStore initialize() {
        userTopics.getUsersCreated()
            .groupByKey()
            .aggregate(
                () -> null,
                (key, user, aggregate) -> user,
                materialized()
            );

        return this;
    }

    public void query(KafkaStreams streams) {
        ReadOnlyKeyValueStore<String, User> store = waitUntilStoreIsReady(USERS_STORE, keyValueStore(), streams);
        log.info("Fetching user 1: " + store.get("1"));
        log.info("Fetching user 2: " + store.get("2"));
        log.info("Fetching user 3: " + store.get("3"));
    }

    private Materialized<String, User, KeyValueStore<Bytes, byte[]>> materialized() {
        return Materialized.<String, User, KeyValueStore<Bytes, byte[]>>as(USERS_STORE).
            withKeySerde(Serdes.String()).
            withValueSerde(userTopics.userSerde());
    }
}
