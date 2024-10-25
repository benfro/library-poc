package net.benfro.library.core.domain;

import java.util.List;

public interface ValuedEnum<T extends Enum<T> & ValuedEnum<T>> {

    int getValue();

    List<T> getValues();

    default int toValue() {
        return getValue();
    }

    default T fromValue(int value) {
        return getValues().stream().filter(e -> e.getValue() == value).findFirst().orElse(null);
    }

}
