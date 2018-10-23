package uk.co.kenfos.users;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.WindowStore;

import static java.util.concurrent.TimeUnit.SECONDS;
import static uk.co.kenfos.users.UsersStreamConfig.USERS_COUNT_STORE;

@Slf4j
@AllArgsConstructor
public class UsersStreamWindowExample {

    private final UserStreams userStreams;

    public void main(String[] args) {
        KTable<Windowed<String>, Long> usersCount = userStreams.getUsersCreated()
            .groupByKey()
            .windowedBy(TimeWindows.of(SECONDS.toMillis(10)))
            .aggregate(
                () -> 0L,
                (key, user, count) -> count + 1,
                materialized()
            );

        usersCount.toStream().foreach((window, count) -> log.info("id: " + window.key() + " count: " + count));
    }

    private Materialized<String, Long, WindowStore<Bytes, byte[]>> materialized() {
        return Materialized.<String, Long, WindowStore<Bytes, byte[]>>as(USERS_COUNT_STORE).
            withKeySerde(Serdes.String()).
            withValueSerde(Serdes.Long());
    }

}
