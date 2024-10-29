package net.benfro.library.core.repository;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DbBorrower {
    private Long id;
    private String borrowerId;
}
