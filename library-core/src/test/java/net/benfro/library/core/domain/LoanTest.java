package net.benfro.library.core.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LoanTest {

    @Test
    void testInterface() {
        assertEquals(Loan.Type.CUSTOM, Loan.Type.LONG_TIME.fromValue(40));
    }
}
