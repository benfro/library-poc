package net.benfro.library.userhub.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class Person {

    private Long id;

    @With
    private Payload payload;

    @Builder
    public record Payload(
        @With @Getter String firstName,
        @With @Getter String lastName,
        @With @Getter String email,
        @With @Getter UUID personId
    ) {

    }

}
