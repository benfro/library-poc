package net.benfro.library.bookhub.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.benfro.library.bookhub.domain.Author;
import net.benfro.library.bookhub.domain.Book;
import net.benfro.library.bookhub.repository.AuthorMapper;
import net.benfro.library.bookhub.repository.AuthorRepository;
import net.benfro.library.bookhub.repository.BookRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookhubService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public Mono<Author> findAuthorById(Long id) {
        return authorRepository.getById(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Author not found")))
            .map(author -> Tuples.of(author, bookRepository.findAllByAuthorId(author.getId()).collectList()))
            .publishOn(Schedulers.boundedElastic())
            .flatMap(tuple -> Mono.just(AuthorMapper.INSTANCE.authorWithBooksToAuthor(
                tuple.getT1(), tuple.getT2().block()))
            );

    }


}
