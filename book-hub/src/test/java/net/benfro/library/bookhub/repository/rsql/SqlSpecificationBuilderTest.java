package net.benfro.library.bookhub.repository.rsql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SqlSpecificationBuilderTest {

    SqlSpecificationBuilder instance;

    @BeforeEach
    void setUp() {
       instance = new SqlSpecificationBuilder();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "firstName==john;lastName==doe | where firstName = 'john' and lastName = 'doe'",
        "firstName==john and lastName==doe | where firstName = 'john' and lastName = 'doe'",
        "firstName==joh* and lastName==doe | where firstName like 'joh%' and lastName = 'doe'",
        "firstName==john | where firstName = 'john'",
    }, delimiter = '|')
    void blaha(String query, String expected) {
        String result = instance.buildSql(query);
        assertEquals(expected, result);
    }
}
