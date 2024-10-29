package net.benfro.library.core.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import net.benfro.library.core.test.IntegrationTest;

class BorrowerRepositoryTest implements IntegrationTest {

    @Autowired
    private BorrowerRepository borrowerRepository;

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
        var id1 = borrowerRepository.reserveId().block();
        var id2 = borrowerRepository.reserveId().block();

        // then
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    void getById_should_return_empty_if_book_does_not_exist() {
        // given
        // no book

        // when
        var result = borrowerRepository.findById(1234L).blockOptional();

        // then
        assertThat(result).isEmpty();
    }

}
