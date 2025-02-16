package net.benfro.library.userhub.person.application.ports.incoming;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@AllArgsConstructor
public class AddNewPersonCommand {

    private Long id;
    @With
    private String firstName;
    @With
    private String lastName;
    @With
    private String email;
    @With
    private UUID personId;
}
