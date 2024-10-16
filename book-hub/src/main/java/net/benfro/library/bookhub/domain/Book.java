package net.benfro.library.bookhub.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class Book {

    private Long id;

    private Payload payload;

    @Builder
    public record Payload(
            @Getter String title,
            @Getter String author,
            @Getter String publisher,
            @Getter String isbn) {

    }
}
