package net.benfro.library.userhub.person.application.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.benfro.library.userhub.person.application.ports.outgoing.kafka.UserEventMessage;

/**
 * Sent to Kafka on user events
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEventMessageImpl implements UserEventMessage {

    private String firstName;
    private String lastName;
    private String email;
    private UUID personId;


}
