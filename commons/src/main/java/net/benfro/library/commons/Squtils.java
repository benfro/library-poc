package net.benfro.library.commons;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Squtils {

    @RequiredArgsConstructor
    static class TableInfo {
        final String name;
        final FieldInfo id;
        final List<FieldInfo> fields;
        private Map<String, FieldInfo> name2info;

        String getIdFieldName() {
            return id.fieldName();
        }

        String getFieldNamesCsv() {
            return fields.stream().map(FieldInfo::fieldName).collect(Collectors.joining(", "));
        }

        FieldInfo getFieldInfo(String fieldName) {
            if (name2info == null) {
                final List<FieldInfo> fieldInfos = Lists.newArrayList(fields);
                fieldInfos.add(id);
                name2info = fieldInfos.stream().collect(Collectors.toMap(FieldInfo::fieldName, Functions.identity()));
            }
            return name2info.get(fieldName);
        }
    }

    private static final String INSERT = "insert into ${table} (${id}, ${fields}) values (${param_id}, ${params_fields})";
    private static final String UPDATE = "update ${table} set ${update_params_fields} where ${id}=${param_id}";
    private static final String SELECT_ALL = "select ${id}, ${fields} from ${table}";
    private static final String SELECT_BY_ID = SELECT_ALL + " where ${id}=${param_id}";
    private static final String SELECT_BY_IDS = SELECT_ALL + " where ${id} in (${param_id}s)";
    private static final String DELETE_BY_ID = "delete from ${table} where ${id}=${param_id}";
    private static final String DELETE_BY_FIELD_STUB = "delete from ${table} where ";
    private final Map<String, String> values;
    private final StringSubstitutor sub;
    private TableInfo tableInfo;

    public Squtils(String table, FieldInfo id, FieldInfo... fields) {
        tableInfo = new TableInfo(table, id, Lists.newArrayList(fields));
        this.values = initValueMap(tableInfo.name, tableInfo.getIdFieldName(), tableInfo.getFieldNamesCsv());
        sub = new StringSubstitutor(values);
    }

    public Squtils(String table, String id, String commaSeparatedFields) {
        this.values = initValueMap(table, id, commaSeparatedFields);
        sub = new StringSubstitutor(values);
    }

    private Map<String, String> initValueMap(String table, String id, String commaSeparatedFields) {
        Map<String, String> values = Maps.newHashMap();
        values.put("fields", trimFieldsCommaSeparatedString(commaSeparatedFields));
        values.put("table", table.trim());
        values.put("id", id.trim());
        values.put("param_id", ":" + values.get("id"));
        values.put("params_fields", getParamsFields(values));
        values.put("update_params_fields", getUpdateParamsFields(values));
        return values;
    }

    @NotNull
    private String trimFieldsCommaSeparatedString(String commaSeparatedFields) {
        return Arrays.stream(commaSeparatedFields.split(","))
                .map(String::trim)
                .collect(Collectors.joining(", "));
    }

    @NotNull
    private String getUpdateParamsFields(Map<String, String> values) {
        return Arrays.stream(values.get("fields")
                        .split(","))
                .map(String::trim)
                .map(s -> s + "=:" + s)
                .collect(Collectors.joining(", "));
    }

    @NotNull
    private String getParamsFields(Map<String, String> values) {
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
        final boolean quoted = tableInfo.getFieldInfo(trim).isQuoted();
        return sub.replace(DELETE_BY_FIELD_STUB + (quoted ? String.format("%s=':%s'", trim, trim) : String.format("%s=:%s", trim, trim)));
    }
}
