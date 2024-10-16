package net.benfro.library.bookhub.repository;

import io.r2dbc.spi.Readable;
import net.benfro.library.bookhub.domain.Book;
import net.benfro.library.bookhub.repository.sql.BookQueries;
import net.benfro.library.bookhub.repository.sql.Squtils;
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

    private final Squtils sql = new Squtils("book", "id", "title, author, publisher, isbn");
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

        return db.sql(sql.insert())
                .bind("id", book.getId())
                .bindProperties(book.getPayload())
                .then();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Void> update(Book book) {
        Validate.notNull(book, "Book can't be null");

        return db.sql(sql.update())
                .bind("id", book.getId())
                .bindProperties(book.getPayload())
                .then();
    }

    /**
     * Get all books.
     * @return A {@link Flux} of all books.
     */
    public Flux<Book> all() {
        return db.sql(sql.selectAll())
                .map(rowToBook())
                .all();
    }

    public Mono<Book> findByISBN(String isbn) {
        Validate.notEmpty(isbn, "ISBN can't be null or empty");
        return db.sql(sql.selectAllWhere("isbn"))
//        return db.sql(BookQueries.SELECT_BY_ISBN)
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
                .payload(Book.Payload.builder()
                        .title(r.get("title", String.class))
                        .author(r.get("author", String.class))
                        .publisher(r.get("publisher", String.class))
                        .isbn(r.get("isbn", String.class))
                        .build())
                .build();
    }
}
