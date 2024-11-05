package net.benfro.library.userhub.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserApplicationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishUserApplicationEvent(UserApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
