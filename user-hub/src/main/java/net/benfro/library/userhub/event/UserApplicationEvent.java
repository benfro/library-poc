package net.benfro.library.userhub.event;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.benfro.library.userhub.model.Person;

/**
 * Published in the application on user events
 */
@Getter
@Builder
@RequiredArgsConstructor
public class UserApplicationEvent {

    public static UserApplicationEvent ofAdded(Person.Payload payload) {
        return new UserApplicationEvent(payload, Action.USER_ADDED);
    }

    public enum Action {
        USER_ADDED,
        USER_UPDATED,
        USER_DELETED;
    }

    private final Person.Payload payload;
    private final Action action;

}
