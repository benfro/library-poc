package net.benfro.library.bookhub.repository.rsql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

class SqlSpecificationBuilderTest {

    @ParameterizedTest
    @CsvSource(value = {
        "firstName==john;lastName==doe | where firstName like 'john' and lastName like 'doe'",
        "firstName==john and lastName==doe | where firstName like 'john' and lastName like 'doe'",
        "firstName==john | where firstName like 'john'",
    }, delimiter = '|')
    void blaha(String query, String expected) {
        Node rootNode = new RSQLParser().parse(query);
        String result = rootNode.accept(new CustomRsqlVisitor());
        assertEquals(expected, result);
    }
}
