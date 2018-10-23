package uk.co.kenfos.users;

import lombok.AllArgsConstructor;
import org.apache.kafka.streams.kstream.Produced;
import uk.co.kenfos.model.User;

import java.util.Objects;

import static org.apache.kafka.common.serialization.Serdes.String;
import static uk.co.kenfos.users.UsersStreamConfig.USERS_ACTIVE;

@AllArgsConstructor
public class ActiveUsersStreamExample {

    private final UserStreams userStreams;

    public void main(String[] args) {
        userStreams.getUsersCreated().leftJoin(userStreams.getUsersDeleted(), this::joinWhenNotEqual)
            .filter((id, user) -> !Objects.isNull(user))
            .to(USERS_ACTIVE, Produced.with(String(), userStreams.userSerde()));
    }

    private User joinWhenNotEqual(User user1, User user2) {
        return Objects.equals(user1, user2) ? null : user1;
    }
}
