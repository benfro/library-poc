package net.benfro.library.commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqutilsTest {

    Squtils instance;

    @BeforeEach
    void setUp() {
        instance = new Squtils("book",
                FieldInfo.ofLong("id"),
                FieldInfo.ofString("stringField"),
                FieldInfo.ofInt("intField"));
    }

    @Test
    void insert() {
        assertEquals("insert into book (id, stringField, intField) values (:id, :stringField, :intField)", instance.insert());
    }

    @Test
    void update() {
        assertEquals("update book set stringField=:stringField, intField=:intField where id=:id", instance.update());
    }

    @Test
    void selectAll() {
        assertEquals("select id, stringField, intField from book", instance.selectAll());
    }

    @Test
    void selectById() {
        assertEquals("select id, stringField, intField from book where id=:id", instance.selectById());
    }

    @Test
    void selectByIds() {
        assertEquals("select id, stringField, intField from book where id in (:ids)", instance.selectByIds());
    }

    @Test
    void selectAllWhere() {
        assertEquals("select id, stringField, intField from book where intField=:intField", instance.selectAllWhere("intField"));
        assertEquals("select id, stringField, intField from book where intField=:intField", instance.selectAllWhere("  intField "));
    }

    @Test
    void deleteByFieldId() {
        assertEquals("delete from book where id=:id", instance.deleteById());
    }

    @Test
    void deleteByFieldField() {
        assertEquals("delete from book where stringField=':stringField'", instance.deleteByField("stringField"));
        assertEquals("delete from book where stringField=':stringField'", instance.deleteByField("  stringField "));
        assertEquals("delete from book where intField=:intField", instance.deleteByField("  intField "));
    }

}