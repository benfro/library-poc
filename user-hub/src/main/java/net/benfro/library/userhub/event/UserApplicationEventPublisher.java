package net.benfro.library.userhub.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserApplicationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishUserApplicationEvent(UserApplicationEvent event) {
        log.debug("Publishing user application event: {}", event);
        applicationEventPublisher.publishEvent(event);
    }
}
