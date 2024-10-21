package net.benfro.library.bookhub.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import net.benfro.library.bookhub.DataGenerator;
import net.benfro.library.bookhub.domain.Author;
import net.benfro.library.bookhub.domain.Book;
import net.benfro.library.bookhub.test.IntegrationTest;

class BookRepositoryTest implements IntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ReactiveTransactionManager transactionManager;

    private TransactionalOperator tx;

    @BeforeEach
    void setup() {
        tx = TransactionalOperator.create(transactionManager);
    }

    @Test
    void reserveId_should_return_unique_id() {
        // given
        var id1 = bookRepository.reserveId().block();
        var id2 = bookRepository.reserveId().block();

        // then
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    void getById_should_return_empty_if_book_does_not_exist() {
        // given
        // no book

        // when
        var result = bookRepository.getById(1234L).blockOptional();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void getById_should_return_persisted_book() {
        // given

        var authorId = authorRepository.reserveId().block();
        var author = Author.builder().id(authorId).firstName("bu").lastName("bae").build();
        authorRepository.persist(author)
            .as(tx::transactional)
            .block();

        var id = bookRepository.reserveId().block();
        var book = Book.builder()
            .id(id)
            .title("John")
            .author("Doe")
            .publisher("john.doe@example.com")
            .isbn(DataGenerator.ISBN())
            .authorId(authorId)
            .build();

        // when
        bookRepository.persist(book)
            .as(tx::transactional)
            .block();

        // then
        var persistedBook = bookRepository.getById(id).block();

        assertThat(persistedBook).isEqualTo(book);
    }

    @Test
    void getByIds_should_return_persisted_books() {
        // given

        var authorId = authorRepository.reserveId().block();
        var author = Author.builder().id(authorId).firstName("bu").lastName("bae").build();
        authorRepository.persist(author)
            .as(tx::transactional)
            .block();

        var id = bookRepository.reserveId().block();
        var book = Book.builder()
            .id(id)
            .title("John")
            .author("Doe")
            .publisher("john.doe@example.com")
            .isbn(DataGenerator.ISBN())
            .authorId(authorId)
            .build();

        // when
        bookRepository.persist(book)
            .as(tx::transactional)
            .block();

        // then
        var persistedBooks = bookRepository.getByIds(List.of(id)).collectList().block();

        assertThat(persistedBooks).hasSize(1);
        assertThat(persistedBooks.get(0)).isEqualTo(book);
    }

    @Test
    void findByEmail_should_return_empty_if_book_does_not_exist() {
        // given
        // no Book

        // when
        var result = bookRepository.findByISBN("foo@bar.com").blockOptional();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void findByISBN_should_return_matching_book() {
        // given
        var authorId = authorRepository.reserveId().block();
        var author = Author.builder().id(authorId).firstName("bu").lastName("bae").build();
        authorRepository.persist(author)
            .as(tx::transactional)
            .block();

        String isbn = DataGenerator.ISBN();
        var book = Book.builder()
            .id(1234L)
            .title("John")
            .author("Doe")
            .publisher("john.doe@example.com")
            .isbn(isbn)
            .authorId(authorId)
            .build();

        bookRepository.persist(book)
            .as(tx::transactional)
            .block();

        // when
        var retrievedBook = bookRepository.findByISBN(isbn).block();

        // then
        assertThat(retrievedBook).isEqualTo(book);
    }

    @Test
    void update_should_update_book() {
        // given
        var authorId = authorRepository.reserveId().block();
        var author = Author.builder().id(authorId).firstName("bu").lastName("bae").build();
        authorRepository.persist(author)
            .as(tx::transactional)
            .block();

        var id = bookRepository.reserveId().block();
        bookRepository.persist(Book.builder()
                .id(id)
                .title("John")
                .author("Doe")
                .publisher("john.doe@example.com")
                .isbn(DataGenerator.ISBN())
                .authorId(authorId)
                .build())
            .as(tx::transactional)
            .block();

        var updatedBook = Book.builder()
            .id(id)
            .title("Johnny")
            .author("Dough")
            .publisher("johnny.dough@example.com")
            .isbn(DataGenerator.ISBN())
            .authorId(authorId)
            .build();

        // when
        bookRepository.update(updatedBook)
            .as(tx::transactional)
            .block();

        // then
        var updated = bookRepository.getById(id).block();
        assertThat(updated).isEqualTo(updatedBook);
    }

}
