package net.benfro.library.core.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Borrower {
    private Long id;
    private UUID borrowerId;
}
