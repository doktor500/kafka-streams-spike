package uk.co.kenfos.users;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreType;
import uk.co.kenfos.utils.TimeUtils;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class StreamUtils {

    // borrowed from https://docs.confluent.io/current/streams/faq.html
    public static <T> T waitUntilStoreIsReady(final String storeName,
                                              final QueryableStoreType<T> queryableStoreType,
                                              final KafkaStreams streams) {
        while (true) {
            try {
                return streams.store(storeName, queryableStoreType);
            } catch (InvalidStateStoreException exception) {
                TimeUtils.sleepFor(1, SECONDS);
            }
        }
    }

}
