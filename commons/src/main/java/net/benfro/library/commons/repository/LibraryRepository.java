package net.benfro.library.commons.repository;

import java.io.Serializable;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LibraryRepository<T, ID extends Serializable> {

    Mono<T> findById(ID id);

    Flux<T> findByIds(Iterable<ID> ids);

    Flux<T> findAll();

    Mono<Void> save(T t);

    Mono<Void> saveAll(Iterable<T> ts);

    Mono<Void> delete(T t);

    Mono<Void> update(T t, ID id);

}
