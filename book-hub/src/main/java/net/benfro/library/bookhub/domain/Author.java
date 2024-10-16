package net.benfro.library.bookhub.domain;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class Author {

    private Long id;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    @Singular
    private Collection<Book> books;
}
