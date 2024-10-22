package net.benfro.library.commons.rsql;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;

import java.util.List;
import java.util.Objects;

public class SqlSpecificationBuilder {

    private final RSQLParser rsqlParser = new RSQLParser();

    public String buildSql(String query) {
        Node rootNode = rsqlParser.parse(query);
        return "where " + rootNode.accept(new CustomRsqlVisitor());
    }

    String buildSql(Node node) {
//        String result = "where ";
        String result = "";
        if (node instanceof LogicalNode logicalNode) {
            result += buildSql(logicalNode);
        }
        if (node instanceof ComparisonNode comparisonNode) {
            result += buildSql(comparisonNode);
        }
        return result;
    }

    private String buildSql(LogicalNode logicalNode) {
        List<String> specs = logicalNode.getChildren()
            .stream()
            .map(this::buildSql)
            .filter(Objects::nonNull)
            .toList();

        String result = specs.getFirst();
        if (logicalNode.getOperator() == LogicalOperator.AND) {
            for (int i = 1; i < specs.size(); i++) {
                result = String.format("%s and %s", result, specs.get(i));
            }
        } else if (logicalNode.getOperator() == LogicalOperator.OR) {
            for (int i = 1; i < specs.size(); i++) {
                result = String.format("%s or %s", result, specs.get(i));
            }
        }

        return result;
    }

    private String buildSql(ComparisonNode comparisonNode) {
        return
            new SqlSpecification(
                comparisonNode.getSelector(),
                comparisonNode.getOperator(),
                comparisonNode.getArguments()
            ).toPredicate();
    }
}
