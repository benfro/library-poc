package net.benfro.library.core.repository;

import java.util.UUID;

import lombok.Data;

@Data
public class DbBorrower {
    private Long id;
    private String borrowerId;
}
