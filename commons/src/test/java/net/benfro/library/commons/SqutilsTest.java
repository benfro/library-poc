package net.benfro.library.commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqutilsTest {

    Squtils instance;

    @BeforeEach
    void setUp() {
        instance = new Squtils("book", "id", "  field1,   field2");
    }

    @Test
    void insert() {
        assertEquals("insert into book (id, field1, field2) values (:id, :field1, :field2)", instance.insert());
    }

    @Test
    void update() {
        assertEquals("update book set field1=:field1, field2=:field2 where id=:id", instance.update());
    }

    @Test
    void selectAll() {
        assertEquals("select id, field1, field2 from book", instance.selectAll());
    }

    @Test
    void selectById() {
        assertEquals("select id, field1, field2 from book where id=:id", instance.selectById());
    }

    @Test
    void selectByIds() {
        assertEquals("select id, field1, field2 from book where id in (:ids)", instance.selectByIds());
    }

    @Test
    void selectAllWhere() {
        assertEquals("select id, field1, field2 from book where field2=:field2", instance.selectAllWhere("field2"));
        assertEquals("select id, field1, field2 from book where field2=:field2", instance.selectAllWhere("  field2 "));
    }

    @Test
    void deleteByFieldId() {
        assertEquals("delete from book where id=:id", instance.deleteById());
    }

    @Test
    void deleteByFieldField() {
        assertEquals("delete from book where field1=:field1", instance.deleteByField("field1"));
        assertEquals("delete from book where field1=:field1", instance.deleteByField("  field1 "));
    }

}