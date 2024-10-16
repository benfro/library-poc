package net.benfro.library.userhub.repository.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public enum PersonQueries implements Supplier<String> {

    // language=sql
    RESERVE_ID("""
            select nextval('person_id_seq')
            """);

    private final String query;

    @Override
    public String get() {
        return query;
    }
}
