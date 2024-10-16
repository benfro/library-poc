package net.benfro.library.bookhub.repository.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public enum AuthorQueries implements Supplier<String> {

    // language=sql
    RESERVE_ID("""
            select nextval('author_id_seq')
            """);

    private final String query;

    @Override
    public String get() {
        return query;
    }
}
