package net.benfro.library.bookhub.repository.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public enum BookSql implements Supplier<String> {

    // language=sql
    RESERVE_ID("""
            select nextval('book_id_seq')
            """),
    // language=sql
    PERSIST("""
            insert into books (id, title, author, publisher, isbn) 
            values (:is, :title, :author, :publisher, :isbn)
            """),
    // language=sql
    SELECT_BY_ID("""
            select id, title, author, publisher, isbn 
            from books where id=:id
            """),
    // language=sql
    SELECT_BY_IDS("""
            select id, title, author, publisher, isbn 
            from books where id in (:ids)
            order by title asc
            """),
    // language=sql
    SELECT_ALL("""
            select id, title, author, publisher, isbn 
            from books order by title asc
            """),
    ;

    private final String sql;

    @Override
    public String get() {
        return sql;
    }
}
