package net.benfro.library.core.repository;

import java.util.UUID;

import lombok.Data;
import net.benfro.library.core.domain.Resource;

@Data
public class DbResource {
    private Long id;
    private String resourceId;
    private int type;
}
