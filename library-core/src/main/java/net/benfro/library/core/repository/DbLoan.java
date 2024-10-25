package net.benfro.library.core.repository;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DbLoan {
    private Long id;
    private LocalDate loanTime;
    private int type;
}
