package net.benfro.library.bookhub.repository;

import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.r2dbc.spi.Readable;
import net.benfro.library.bookhub.domain.Book;
import net.benfro.library.bookhub.repository.sql.BookQueries;
import net.benfro.library.bookhub.repository.sql.Squtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BookRepository {

    private final Squtils sql = new Squtils("book", "id", "title, author, publisher, isbn, author_id");
    private final DatabaseClient db;

    @Autowired
    public BookRepository(DatabaseClient db) {
        this.db = db;
    }

    public Mono<Long> reserveId() {
        return db.sql(BookQueries.RESERVE_ID)
            .mapValue(Long.class)
            .one();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Void> persist(Book book) {
        Validate.notNull(book, "book can't be null");

        DbBook dbBook = BookMapper.INSTANCE.toDbBook(book);
        return db.sql(sql.insert())
            .bindProperties(dbBook)
            .then();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Void> update(Book book) {
        Validate.notNull(book, "Book can't be null");

        DbBook dbBook = BookMapper.INSTANCE.toDbBook(book);
        return db.sql(sql.update())
            .bindProperties(dbBook)
            .then();
    }

    /**
     * Get all books.
     *
     * @return A {@link Flux} of all books.
     */
    public Flux<Book> all() {
        return db.sql(sql.selectAll())
            .map(rowToBook())
            .all();
    }

    public Flux<Book> findAllByAuthorId(Long authroId) {
        return db.sql(sql.selectAll() + " where author_id = :authorId")
            .map(rowToBook())
            .all();
    }

    public Mono<Book> findByISBN(String isbn) {
        Validate.notEmpty(isbn, "ISBN can't be null or empty");
        return db.sql(sql.selectAllWhere("isbn"))
            .bind("isbn", isbn)
            .map(rowToBook())
            .one();
    }

    public Mono<Book> getById(Long id) {
        Validate.notNull(id, "id can't be null");

        return db.sql(sql.selectById())
            .bind("id", id)
            .map(rowToBook())
            .one();
    }

    public Flux<Book> getByIds(List<Long> ids) {
        Validate.notNull(ids, "ids can't be null");

        if (ids.isEmpty()) {
            return Flux.empty();
        }

        return db.sql(sql.selectByIds())
            .bind("ids", ids)
            .map(rowToBook())
            .all();
    }

    private Function<Readable, Book> rowToBook() {
        return r -> Book.builder()
            .id(r.get("id", Long.class))
            .title(r.get("title", String.class))
            .author(r.get("author", String.class))
            .publisher(r.get("publisher", String.class))
            .isbn(r.get("isbn", String.class))
            .authorId(r.get("author_id", Long.class))
            .build();
    }
}
