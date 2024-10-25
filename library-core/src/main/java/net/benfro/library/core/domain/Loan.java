package net.benfro.library.core.domain;

import java.time.LocalDate;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class Loan {

    @Getter
    @RequiredArgsConstructor
    public enum Type implements ValuedEnum<Type> {
        STANDARD(10),
        LONG_TIME(20),
        SHORT_TIME(30),
        CUSTOM(40);

        private final int value;

        @Override
        public List<Type> getValues() {
            return Lists.newArrayList(values());
        }
    }

    private Long id;
    private LocalDate loanTime;
    private Type type;
}
