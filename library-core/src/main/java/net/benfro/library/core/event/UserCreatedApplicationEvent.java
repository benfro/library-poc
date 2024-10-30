package net.benfro.library.core.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;
import net.benfro.library.core.message.UserEvent;

@Getter
public class UserCreatedApplicationEvent extends ApplicationEvent {

    private final UserEvent payload;

    public UserCreatedApplicationEvent(Object source, UserEvent payload) {
        super(source);
        this.payload = payload;
    }
}
