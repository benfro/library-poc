package net.benfro.library.userhub.person.application.ports.outgoing.kafka;

import java.util.UUID;

public interface UserEventMessage {
    String getFirstName();

    String getLastName();

    String getEmail();

    UUID getPersonId();

    void setFirstName(String firstName);

    void setLastName(String lastName);

    void setEmail(String email);

    void setPersonId(UUID uuid);
}
