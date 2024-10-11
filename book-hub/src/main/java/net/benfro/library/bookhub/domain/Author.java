package net.benfro.library.bookhub.domain;

import java.util.Collection;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class Author {

    private Long id;

    private String firstName;

    private String lastName;

    @Singular
    private Collection<Long> books;
}
