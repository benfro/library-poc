package net.benfro.library.userhub.model;


import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
public class Person {

    private Long id;

    private Payload payload;

    @Builder
    public record Payload(@With @Getter String firstName, @With @Getter String lastName, @With @Getter String email) {

    }

}
