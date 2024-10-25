package net.benfro.library.core.domain.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import net.benfro.library.core.domain.Loan;
import net.benfro.library.core.repository.DbLoan;

class LoanMapperTest {

    @Test
    void testToDbLoan() {
        Loan loan = Loan.builder().loanTime(LocalDate.now()).id(1L).type(Loan.Type.CUSTOM).build();

        DbLoan dbLoan = LoanMapper.INSTANCE.toDbLoan(loan);
        assertEquals(40, dbLoan.getType());
    }

    @Test
    void testToLoan() {
        DbLoan dbloan = DbLoan.builder().loanTime(LocalDate.now()).id(1L).type(40).build();

        Loan loan = LoanMapper.INSTANCE.toLoan(dbloan);
        assertEquals(Loan.Type.CUSTOM, loan.getType());
    }
}
