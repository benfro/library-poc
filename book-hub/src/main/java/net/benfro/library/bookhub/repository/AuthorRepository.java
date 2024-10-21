package net.benfro.library.bookhub.repository;

import io.r2dbc.spi.Readable;
import net.benfro.library.bookhub.domain.Author;
import net.benfro.library.bookhub.repository.sql.AuthorQueries;
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
public class AuthorRepository {

    private final Squtils sql = new Squtils("author", "id", "first_name, last_name");
    private final DatabaseClient db;

    @Autowired
    public AuthorRepository(DatabaseClient db) {
        this.db = db;
    }

    public Mono<Long> reserveId() {
        return db.sql(AuthorQueries.RESERVE_ID)
                .mapValue(Long.class)
                .one();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Void> persist(Author author) {
        Validate.notNull(author, "author can't be null");

        return db.sql(sql.insert())
                .bind("id", author.getId())
                .bind("first_name", author.getFirstName())
                .bind("last_name", author.getLastName())
                .then();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Mono<Void> update(Author author) {
        Validate.notNull(author, "Author can't be null");

        return db.sql(sql.update())
                .bind("id", author.getId())
                .bind("first_name", author.getFirstName())
                .bind("last_name", author.getLastName())
                .then();
    }

    /**
     * Get all books.
     * @return A {@link Flux} of all Authors.
     */
    public Flux<Author> all() {
        return db.sql(sql.selectAll())
                .map(rowToAuthor())
                .all();
    }

//    public Mono<Author> findByISBN(String isbn) {
//        Validate.notEmpty(isbn, "ISBN can't be null or empty");
//        return db.sql(sql.selectAllWhere("isbn"))
//                .bind("isbn", isbn)
//                .map(rowToBook())
//                .one();
//    }

    public Mono<Author> getById(Long id) {
        Validate.notNull(id, "id can't be null");

        return db.sql(sql.selectById())
                .bind("id", id)
                .map(rowToAuthor())
                .one();
    }

    public Flux<Author> getByIds(List<Long> ids) {
        Validate.notNull(ids, "ids can't be null");

        if (ids.isEmpty()) {
            return Flux.empty();
        }

        return db.sql(sql.selectByIds())
                .bind("ids", ids)
                .map(rowToAuthor())
                .all();
    }

    private Function<Readable, Author> rowToAuthor() {
        return r -> Author.builder()
                .id(r.get("id", Long.class))
                .firstName(r.get("first_name", String.class))
                .lastName(r.get("last_name", String.class))
                .build();
    }
}
