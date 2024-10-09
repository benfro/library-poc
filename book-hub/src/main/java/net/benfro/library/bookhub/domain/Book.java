package net.benfro.library.bookhub.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class Book {

    private Long id;

    private Payload payload;

    @Builder
    public record Payload(String title, String author, String publisher, String isbn) {

    }
}
