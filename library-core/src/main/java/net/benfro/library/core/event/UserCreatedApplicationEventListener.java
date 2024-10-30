package net.benfro.library.core.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedApplicationEventListener implements ApplicationListener<UserCreatedApplicationEvent> {
    @Override
    public void onApplicationEvent(UserCreatedApplicationEvent event) {

    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
