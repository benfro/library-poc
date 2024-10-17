package net.benfro.library.bookhub.repository.rsql;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

public class CustomRsqlVisitor implements RSQLVisitor<String, Void> {

    private SqlSpecificationBuilder builder;

    public CustomRsqlVisitor() {
        builder = new SqlSpecificationBuilder();
    }

    @Override
    public String visit(AndNode andNode, Void unused) {
        return builder.buildSql(andNode);
    }

    @Override
    public String visit(OrNode orNode, Void unused) {
        return builder.buildSql(orNode);
    }

    @Override
    public String visit(ComparisonNode comparisonNode, Void unused) {
        return builder.buildSql(comparisonNode);
    }
}
