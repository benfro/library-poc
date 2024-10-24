package net.benfro.library.bookhub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class Book {

    private Long id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String publishYear;
    @JsonProperty("author_id")
    @Builder.Default
    private Long authorId = -1L;


}
