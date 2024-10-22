package net.benfro.library.commons.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SqlSpecification {

    private String property;
    private ComparisonOperator operator;
    private List<String> arguments;

    public String toPredicate() {
        if (!arguments.isEmpty()) {
            RsqlSearchOperation simpleOperator = RsqlSearchOperation.getSimpleOperator(operator);
            return switch (Objects.nonNull(simpleOperator) ? simpleOperator : null) {
                case EQUAL -> {
                    if (arguments.getFirst().contains("*")) {
                        yield String.format("%s like '%s'", property, arguments.getFirst().replace('*', '%'));
                    }
                    yield  String.format("%s = '%s'", property, arguments.getFirst());
                }
                case NOT_EQUAL -> {
                    if (arguments.getFirst().contains("*")) {
                        yield  String.format("%s not like '%s'", property, arguments.getFirst().replace('*', '%'));
                    }
                    yield String.format("%s != '%s'", property, arguments.getFirst());
                }
                case GREATER_THAN -> String.format("%s > '%s'", property, arguments.getFirst());
                case GREATER_THAN_OR_EQUAL -> String.format("%s >= '%s'", property, arguments.getFirst());
                case LESS_THAN -> String.format("%s < '%s'", property, arguments.getFirst());
                case LESS_THAN_OR_EQUAL -> String.format("%s <= '%s'", property, arguments.getFirst());
                case IN -> String.format("%s in (%s)",
                    property,
                    arguments.stream().map(String::trim).collect(Collectors.joining(", ")));
                case NOT_IN -> String.format("%s not in (%s)",
                    property,
                    arguments.stream().map(String::trim).collect(Collectors.joining(", ")));
                case null -> throw new IllegalArgumentException("Operator is null");
            };
        }
        return null;
    }

//    private List<Object> castArguments(final Root<T> root) {

//        Class<? extends Object> type = root.get(property).getJavaType();

//        List<Object> args = arguments.stream().map(arg -> {
//            if (type.equals(Integer.class)) {
//                return Integer.parseInt(arg);
//            } else if (type.equals(Long.class)) {
//                return Long.parseLong(arg);
//            } else {
//                return arg;
//            }
//        }).collect(Collectors.toList());

//        return args;
//    }
}
