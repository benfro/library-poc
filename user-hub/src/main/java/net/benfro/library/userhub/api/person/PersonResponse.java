package net.benfro.library.userhub.api.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UUID personId;
}
