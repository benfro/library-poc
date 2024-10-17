package net.benfro.library.bookhub.repository.rsql;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;

public class SqlSpecificationBuilder {

    public String buildSql(Node node) {
        String result = "where ";
        if (node instanceof LogicalNode) {
            result += buildSql((LogicalNode)node);
        }
        if (node instanceof ComparisonNode) {
            result += buildSql((ComparisonNode)node);
        }
        return result;
    }

    private String buildSql(LogicalNode logicalNode) {
        List<String> specs = logicalNode.getChildren()
            .stream()
            .map(node -> buildSql(node))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

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
