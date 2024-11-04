package net.benfro.library.core.message;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
public record UserEvent(
        @Getter Action action,
        @Getter String fullName,
        @Getter String email,
        @Getter UUID userKey) {
    public enum Action {
        USER_CREATED,
        USER_UPDATED,
        USER_DELETED
    }
}
