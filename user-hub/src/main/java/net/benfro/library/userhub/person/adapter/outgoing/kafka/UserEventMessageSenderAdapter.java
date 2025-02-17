package net.benfro.library.userhub.person.adapter.outgoing.kafka;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import net.benfro.library.userhub.person.application.ports.outgoing.kafka.UserEventMessage;
import net.benfro.library.userhub.person.application.ports.outgoing.kafka.UserEventMessageSender;

@Component
@AllArgsConstructor
public class UserEventMessageSenderAdapter implements UserEventMessageSender {

    private final UserEventKafkaProducer kafkaProducer;

    @Override
    public void sendMessage(UserEventMessage msg) {
        kafkaProducer.emitEvent(msg);
    }
}
