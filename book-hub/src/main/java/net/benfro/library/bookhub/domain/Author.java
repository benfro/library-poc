package net.benfro.library.bookhub.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
@Data
@Builder
public class Author {

    private Long id;

    private String name;

    @Singular
    private Collection<Long> books;
}
