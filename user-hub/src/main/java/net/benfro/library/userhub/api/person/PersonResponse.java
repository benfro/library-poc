package net.benfro.library.userhub.api.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
