package uk.co.kenfos.users;

import lombok.AllArgsConstructor;
import org.apache.kafka.streams.kstream.Produced;
import uk.co.kenfos.model.User;

import java.util.Objects;

import static org.apache.kafka.common.serialization.Serdes.String;
import static uk.co.kenfos.users.UsersStreamConfig.ACTIVE_USERS;

@AllArgsConstructor
public class ActiveUsersKStream {

    private final UserTopics userTopics;

    public ActiveUsersKStream initialize() {
        userTopics.getUsersCreated().leftJoin(userTopics.getUsersDeleted(), this::joinWhenNotEqual)
            .filter((id, user) -> !Objects.isNull(user))
            .to(ACTIVE_USERS, Produced.with(String(), userTopics.userSerde()));

        return this;
    }

    private User joinWhenNotEqual(User user1, User user2) {
        return Objects.equals(user1, user2) ? null : user1;
    }
}
