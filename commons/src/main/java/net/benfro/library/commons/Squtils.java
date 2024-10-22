package net.benfro.library.commons;

import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Squtils {

    private static final String INSERT = "insert into ${table} (${id}, ${fields}) values (${param_id}, ${params_fields})";
    private static final String UPDATE = "update ${table} set ${update_params_fields} where ${id}=${param_id}";
    private static final String SELECT_ALL = "select ${id}, ${fields} from ${table}";
    private static final String SELECT_BY_ID = SELECT_ALL + " where ${id}=${param_id}";
    private static final String SELECT_BY_IDS = SELECT_ALL + " where ${id} in (${param_id}s)";
    private static final String DELETE_BY_ID = "delete from ${table} where ${id}=${param_id}";
    private static final String DELETE_BY_FIELD_STUB = "delete from ${table} where ";
    private final Map<String, String> values = new HashMap<>();
    private final StringSubstitutor sub;

    public Squtils(String table, String id, String commaSeparatedFields) {
        values.put("fields", trimFieldsCommaSeparatedString(commaSeparatedFields));
        values.put("table", table.trim());
        values.put("id", id.trim());
        values.put("param_id", ":" + values.get("id"));
        values.put("params_fields", getParamsFields());
        values.put("update_params_fields", getUpdateParamsFields());
        sub = new StringSubstitutor(values);
    }

    @NotNull
    private String trimFieldsCommaSeparatedString(String commaSeparatedFields) {
        return Arrays.stream(commaSeparatedFields.split(","))
                .map(String::trim)
                .collect(Collectors.joining(", "));
    }

    @NotNull
    private String getUpdateParamsFields() {
        return Arrays.stream(values.get("fields")
                .split(","))
                .map(String::trim)
                .map(s -> s + "=:" + s)
                .collect(Collectors.joining(", "));
    }

    @NotNull
    private String getParamsFields() {
        return Arrays.stream(values.get("fields")
                        .split(","))
                .map(s -> ":" + s.trim())
                .collect(Collectors.joining(", "));
    }

    public String insert() {
        return sub.replace(INSERT);
    }

    public String update() {
        return sub.replace(UPDATE);
    }

    public String selectAll() {
        return sub.replace(SELECT_ALL);
    }

    public String selectAllWhere(String fieldEquals) {
        final String trim = fieldEquals.trim();
        return sub.replace(SELECT_ALL) + String.format(" where %s=:%s", trim, trim);
    }

    public String selectById() {
        return sub.replace(SELECT_BY_ID);
    }

    public String selectByIds() {
        return sub.replace(SELECT_BY_IDS);
    }

    public String deleteById() {
        return sub.replace(DELETE_BY_ID);
    }

    public String deleteByField(String fieldToDeleteBy) {
        final String trim = fieldToDeleteBy.trim();
        return sub.replace(DELETE_BY_FIELD_STUB + String.format("%s=:%s", trim, trim));
    }
}
