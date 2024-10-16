package net.benfro.library.bookhub.repository;

import net.benfro.library.bookhub.domain.Author;
import net.benfro.library.bookhub.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorRepositoryTest implements IntegrationTest {

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
        var id1 = authorRepository.reserveId().block();
        var id2 = authorRepository.reserveId().block();

        // then
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    void getById_should_return_empty_if_author_does_not_exist() {
        // given
        // no author

        // when
        var result = authorRepository.getById(1234L).blockOptional();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void getById_should_return_persisted_author() {
        // given
        var id = authorRepository.reserveId().block();
        var author = Author.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .build();

        // when
        authorRepository.persist(author)
                .as(tx::transactional)
                .block();

        // then
        var persistedAuthor = authorRepository.getById(id).block();

        assertThat(persistedAuthor).isEqualTo(author);
    }

    @Test
    void getByIds_should_return_persisted_authors() {
        // given
        var id = authorRepository.reserveId().block();
        var author = Author.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .build();

        // when
        authorRepository.persist(author)
                .as(tx::transactional)
                .block();

        // then
        var persistedAuthors = authorRepository.getByIds(List.of(id)).collectList().block();

        assertThat(persistedAuthors).hasSize(1);
        assertThat(persistedAuthors.get(0)).isEqualTo(author);
    }

//    @Test
//    void findByEmail_should_return_empty_if_book_does_not_exist() {
//        // given
//        // no Book
//
//        // when
//        var result = bookRepository.findByISBN("foo@bar.com").blockOptional();
//
//        // then
//        assertThat(result).isEmpty();
//    }

//    @Test
//    void findByISBN_should_return_matching_book() {
//        // given
//        String isbn = DataGenerator.ISBN();
//        var book = Book.builder()
//                .id(1234L)
//                .payload(Book.Payload.builder()
//                        .title("John")
//                        .author("Doe")
//                        .publisher("john.doe@example.com")
//                        .isbn(isbn)
//                        .build())
//                .build();

//        bookRepository.persist(book)
//                .as(tx::transactional)
//                .block();

//        // when
//        var retrievedBook = bookRepository.findByISBN(isbn).block();

//        // then
//        assertThat(retrievedBook).isEqualTo(book);
//    }

    @Test
    void update_should_update_author() {
        // given
        var id = authorRepository.reserveId().block();
        authorRepository.persist(Author.builder()
                        .id(id)
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .as(tx::transactional)
                .block();

        var updatedBook = Author.builder()
                .id(id)
                .firstName("Johnny")
                .lastName("Dough")
                .build();

        // when
        authorRepository.update(updatedBook)
                .as(tx::transactional)
                .block();

        // then
        var updated = authorRepository.getById(id).block();
        assertThat(updated).isEqualTo(updatedBook);
    }

}
