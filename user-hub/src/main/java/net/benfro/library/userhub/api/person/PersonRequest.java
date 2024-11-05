package net.benfro.library.userhub.api.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {
    @With
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UUID personId;
}
