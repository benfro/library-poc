package net.benfro.library.userhub.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.benfro.library.userhub.person.adapter.outgoing.kafka.UserEventKafkaProducer;
import net.benfro.library.userhub.person.application.model.UserEventMapper;

@Slf4j
@Component
@AllArgsConstructor
public class UserApplicationEventListener {

    @Setter
    private UserEventKafkaProducer producer;

    @EventListener
    public void listenForEvent(UserApplicationEvent event) {
        log.debug("Received user event {}", event);
        producer.emitEvent(UserEventMapper.INSTANCE.fromUserToUserEventMessage(event));
    }

}
