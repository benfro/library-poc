package net.benfro.library.userhub.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonRequest {
    @With
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
