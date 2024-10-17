package net.benfro.library.bookhub.repository.rsql;

import java.util.List;
import java.util.stream.Collectors;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SqlSpecification {

    private String property;
    private ComparisonOperator operator;
    private List<String> arguments;

    public String toPredicate() {
        switch (RsqlSearchOperation.getSimpleOperator(operator)) {

            case EQUAL -> {
                if (arguments.size() == 1) {
                    return String.format("%s like '%s'", property, arguments.getFirst().replace('*', '%'));
                } else if (arguments.size() == 1 && arguments.contains("null")) {
                    return property + " is null";
                }
            }
            case NOT_EQUAL -> {
                if (arguments.size() == 1) {
                    return String.format("%s not like '%s'", property, arguments.getFirst().replace('*', '%'));
                } else if (arguments.size() == 1 && arguments.contains("null")) {
                    return property + " is not null";
                }
            }
            case GREATER_THAN -> String.format("%s > '%s'", property, arguments.getFirst());
            case GREATER_THAN_OR_EQUAL ->  String.format("%s >= '%s'", property, arguments.getFirst());
            case LESS_THAN -> String.format("%s < '%s'", property, arguments.getFirst());
            case LESS_THAN_OR_EQUAL -> String.format("%s <= '%s'", property, arguments.getFirst());
            case IN -> String.format("%s in (%s)", property, arguments.stream().map(String::trim).collect(Collectors.joining(", ")));
            case NOT_IN -> String.format("%s not in (%s)", property, arguments.stream().map(String::trim).collect(Collectors.joining(", ")));
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
