package net.benfro.library.bookhub.repository;

import net.benfro.library.bookhub.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import static org.assertj.core.api.Assertions.assertThat;

class BookRepositoryTest implements IntegrationTest {

    @Autowired
    private BookRepository bookRepository;

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

}