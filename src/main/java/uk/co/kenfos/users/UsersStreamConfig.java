package uk.co.kenfos.users;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import uk.co.kenfos.json.JsonSerializer;
import uk.co.kenfos.model.User;

import java.util.Properties;

public class UsersStreamConfig {

    public static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static final String APPLICATION_ID = "users";
    public static final String USERS_ACTIVE = "users-active";
    public static final String USERS_CREATED = "users-created";
    public static final String USERS_DELETED = "users-deleted";
    public static final String USERS_COUNT_STORE = "users-count-store";

    public static Properties properties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", new JsonSerializer<User>().getClass().getName());
        return properties;
    }
}
