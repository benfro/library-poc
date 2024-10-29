package net.benfro.library.core.repository;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Function;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import io.r2dbc.spi.Readable;
import net.benfro.library.commons.FieldInfo;
import net.benfro.library.commons.Squtils;
import net.benfro.library.commons.repository.LibraryRepository;
import net.benfro.library.core.domain.Borrower;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BorrowerRepository implements LibraryRepository<Borrower, Long> {

    private final Squtils sql = new Squtils("borrower", "id", "borrower_id");
    private final DatabaseClient db;

    @Autowired
    public BorrowerRepository(DatabaseClient db) {
        this.db = db;
    }

    public Mono<Long> reserveId() {
        return db.sql("SELECT nextval('borrower_id_seq')")
            .mapValue(Long.class)
            .one();
    }

    @Override
    public Mono<Borrower> findById(Long aLong) {
        Validate.notNull(aLong, "id can't be null");

        return db.sql(sql.selectById())
            .bind("id", aLong)
            .map(rowToBorrower())
            .one();
    }

    @Override
    public Flux<Borrower> findByIds(Iterable<Long> iterable) {
        ArrayList<Long> ids = Lists.newArrayList(iterable);
        if (ids.isEmpty()) {
            return Flux.empty();
        }
        return db.sql(sql.selectByIds())
            .bind("ids", ids)
            .map(rowToBorrower())
            .all();
    }

    @Override
    public Flux<Borrower> findAll() {
        return db.sql(sql.selectAll())
            .map(rowToBorrower())
            .all();
    }

    @Override
    public Mono<Void> save(Borrower borrower) {

        return db.sql(sql.insert())
            .bind("borrowerId", UUID.class)
            .bind("id", Long.class)
            .then();
    }

    @Override
    public Mono<Void> saveAll(Iterable<Borrower> iterable) {

        return Flux.fromStream(Lists.newArrayList(iterable).stream())
            .flatMap(this::save)
            .then();
    }

    @Override
    public Mono<Void> delete(Borrower borrower) {

        return db.sql(sql.deleteById())
            .bind("id", Long.class)
            .then();
    }

    @Override
    public Mono<Void> update(Borrower borrower, Long aLong) {
        return Mono.empty().then();
    }

    private Function<Readable, Borrower> rowToBorrower() {
        return r -> {
            return Borrower.builder()
                .id(r.get("id", Long.class))
                .borrowerId(r.get("borrower_id", UUID.class))
                .build();
        };
    }
}
