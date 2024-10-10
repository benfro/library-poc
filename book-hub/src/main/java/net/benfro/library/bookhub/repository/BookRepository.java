package net.benfro.library.bookhub.repository;

import io.r2dbc.spi.Readable;
import net.benfro.library.bookhub.domain.Book;
import net.benfro.library.bookhub.repository.sql.BookQueries;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Repository
public class BookRepository {

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

        return db.sql(BookQueries.PERSIST)
                .bind("id", book.getId())
                .bind("title", book.getPayload().title())
                .bind("author", book.getPayload().author())
                .bind("publisher", book.getPayload().publisher())
                .bind("isbn", book.getPayload().isbn())
                .then();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Void> update(Book book) {
        Validate.notNull(book, "Book can't be null");

        return db.sql(BookQueries.UPDATE)
                .bind("id", book.getId())
                .bind("title", book.getPayload().title())
                .bind("author", book.getPayload().author())
                .bind("publisher", book.getPayload().publisher())
                .bind("isbn", book.getPayload().isbn())
                .then();
    }

    /**
     * Get all books.
     * @return A {@link Flux} of all books.
     */
    public Flux<Book> all() {
        return db.sql(BookQueries.SELECT_ALL)
                .map(rowToBook())
                .all();
    }

    public Mono<Book> findByISBN(String emailAddress) {
        Validate.notEmpty(emailAddress, "emailAddress can't be null or empty");
        return db.sql(BookQueries.SELECT_BY_ISBN)
                .bind("isbn", emailAddress)
                .map(rowToBook())
                .one();
    }

    public Mono<Book> getById(Long id) {
        Validate.notNull(id, "id can't be null");

        return db.sql(BookQueries.SELECT_BY_ID)
                .bind("id", id)
                .map(rowToBook())
                .one();
    }

    public Flux<Book> getByIds(List<Long> ids) {
        Validate.notNull(ids, "ids can't be null");

        if (ids.isEmpty()) {
            return Flux.empty();
        }

        return db.sql(BookQueries.SELECT_BY_IDS)
                .bind("ids", ids)
                .map(rowToBook())
                .all();
    }

    private Function<Readable, Book> rowToBook() {
        return r -> Book.builder()
                .id(r.get("id", Long.class))
                .payload(Book.Payload.builder()
                        .title(r.get("title", String.class))
                        .author(r.get("author", String.class))
                        .publisher(r.get("publisher", String.class))
                        .isbn(r.get("isbn", String.class))
                        .build())
                .build();
    }
}
