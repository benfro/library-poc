package net.benfro.library.userhub.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.benfro.library.userhub.message.UserEventKafkaProducer;
import net.benfro.library.userhub.message.UserEventMapper;

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
