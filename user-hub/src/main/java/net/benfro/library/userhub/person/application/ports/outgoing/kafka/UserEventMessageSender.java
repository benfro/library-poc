package net.benfro.library.userhub.person.application.ports.outgoing.kafka;

public interface UserEventMessageSender {

    void sendMessage(UserEventMessage msg);
}
