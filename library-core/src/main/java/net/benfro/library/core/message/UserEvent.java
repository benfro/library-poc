package net.benfro.library.core.message;

import java.util.UUID;

import lombok.Getter;

@Getter
public record UserEvent(Action action, String fullName, String email, UUID userKey) {
    public enum Action {
        USER_CREATED,
        USER_UPDATED,
        USER_DELETED
    }
}
