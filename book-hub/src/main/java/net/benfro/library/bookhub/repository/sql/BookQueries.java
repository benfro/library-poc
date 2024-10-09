package net.benfro.library.bookhub.repository.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public enum BookQueries implements Supplier<String> {

    // language=sql
    RESERVE_ID("""
            select nextval('book_id_seq')
            """),
    // language=sql
    PERSIST("""
            insert into book (id, title, author, publisher, isbn) 
            values (:is, :title, :author, :publisher, :isbn)
            """),
    // language=sql
    SELECT_BY_ID("""
            select id, title, author, publisher, isbn 
            from book where id=:id
            """),
    // language=sql
    SELECT_BY_IDS("""
            select id, title, author, publisher, isbn 
            from book where id in (:ids)
            order by title asc
            """),
    // language=sql
    SELECT_ALL("""
            select id, title, author, publisher, isbn 
            from book order by title asc
            """),
    // language=sql
    SELECT_BY_ISBN("""
            select id, title, author, publisher, isbn 
            from book where isbn=:isbn
            """),
    // language=sql
    UPDATE("""
            update book
            set title=:title, author=:author, publisher=:publisher, isbn=:isbn
            where id=:id
            """);

    private final String query;

    @Override
    public String get() {
        return query;
    }
}
