package net.benfro.library.userhub.person.adapter.outgoing.persistence.model;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.With;


@Data
@Builder
@AllArgsConstructor
public class PersonEntity {

    private Long id;

    @With
    private Payload payload;

    @Builder
    public record Payload(
        @With @Getter String firstName,
        @With @Getter String lastName,
        @With @Getter String email,
        @With @Getter UUID personId
    ) {}

}
