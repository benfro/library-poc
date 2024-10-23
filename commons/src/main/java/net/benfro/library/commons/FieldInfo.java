package net.benfro.library.commons;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FieldInfo(String fieldName, Class<?> fieldType) {

    public static FieldInfo ofString(String fieldName) {
        return new FieldInfo(fieldName, String.class);
    }

    public static FieldInfo ofInt(String fieldName) {
        return new FieldInfo(fieldName, Integer.class);
    }

    public static FieldInfo ofLong(String fieldName) {
        return new FieldInfo(fieldName, Long.class);
    }

    public static FieldInfo ofDate(String fieldName) {
        return new FieldInfo(fieldName, LocalDate.class);
    }

    public static FieldInfo ofDateTime(String fieldName) {
        return new FieldInfo(fieldName, LocalDateTime.class);
    }

    public boolean isQuoted() {
        return String.class.equals(fieldType);
    }
}
