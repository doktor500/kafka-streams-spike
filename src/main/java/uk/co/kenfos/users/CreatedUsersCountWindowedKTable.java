package uk.co.kenfos.users;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.WindowStore;

import static java.util.concurrent.TimeUnit.SECONDS;
import static uk.co.kenfos.users.UsersStreamConfig.USERS_COUNT_WINDOW_STORE;

@Slf4j
public class CreatedUsersCountWindowedKTable {

    private final UserTopics userTopics;
    private KTable<Windowed<String>, Long> createdUsers;

    public CreatedUsersCountWindowedKTable(UserTopics userTopics) {
        this.userTopics = userTopics;
    }

    public CreatedUsersCountWindowedKTable initialize() {
        createdUsers = userTopics.getUsersCreated()
            .groupByKey()
            .windowedBy(TimeWindows.of(SECONDS.toMillis(10)))
            .aggregate(
                () -> 0L,
                (key, user, count) -> count + 1,
                materialized()
            );

        return this;
    }

    public CreatedUsersCountWindowedKTable withContinuousQuery() {
        createdUsers.toStream()
            .foreach((window, count) -> log.info("Created user: " + window.key() + ", fetched: " + count + " times"));

        return this;
    }

    private Materialized<String, Long, WindowStore<Bytes, byte[]>> materialized() {
        return Materialized.<String, Long, WindowStore<Bytes, byte[]>>as(USERS_COUNT_WINDOW_STORE).
            withKeySerde(Serdes.String()).
            withValueSerde(Serdes.Long());
    }

}
