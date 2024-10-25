package net.benfro.library.core.domain;

import java.util.UUID;

import lombok.Data;

@Data
public class Borrower {
    private Long id;
    private UUID borrowerId;
}
