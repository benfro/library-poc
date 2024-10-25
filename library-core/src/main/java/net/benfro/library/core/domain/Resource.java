package net.benfro.library.core.domain;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class Resource {

    @Getter
    @RequiredArgsConstructor
    public enum Type implements ValuedEnum<Type> {
        PUBLICATION(10),
        AUDIO(20),
        VIDEO(30),
        OTHER(40);

        private final int value;

        @Override
        public List<Type> getValues() {
            return Lists.newArrayList(values());
        }
    }

    private Long id;
    private UUID resourceId;
    private Type type;
}
