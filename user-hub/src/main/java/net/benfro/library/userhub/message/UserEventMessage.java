package net.benfro.library.userhub.message;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sent to Kafka on user events
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEventMessage {

    private String firstName;
    private String lastName;
    private String email;
    private UUID personId;


}
