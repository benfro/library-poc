package net.benfro.library.bookhub.event;

import org.apache.commons.lang3.Validate;
import org.springframework.context.ApplicationEvent;

public class LoginEventHandledApplicationEvent extends ApplicationEvent {

    private final LoginEvent loginEvent;

    public LoginEventHandledApplicationEvent(Object source, LoginEvent loginEvent) {
        super(source);
        this.loginEvent = Validate.notNull(loginEvent, "loginEvent can't be null");
    }

    public LoginEvent loginEvent() {
        return loginEvent;
    }
}
