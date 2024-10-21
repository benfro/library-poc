package net.benfro.library.bookhub.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class DbBook {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    @JsonProperty("author_id")
    @Builder.Default
    private Long author_id = -1L;
}
