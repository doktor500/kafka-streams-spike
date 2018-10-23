package uk.co.kenfos.users;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import uk.co.kenfos.json.JsonSerializer;
import uk.co.kenfos.model.User;

import java.util.Properties;

public class UsersStreamConfig {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String APPLICATION_ID = "users";

    public static final String ACTIVE_USERS = "users-active";
    public static final String CREATED_USERS = "users-created";
    public static final String DELETED_USERS = "users-deleted";

    public static final String USERS_COUNT_WINDOW_STORE = "users-count-window-store";
    public static final String USERS_STORE = "users-store";

    public static Properties properties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        properties.put("group.id", APPLICATION_ID);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", new JsonSerializer<User>().getClass().getName());
        return properties;
    }
}
