package net.benfro.library.core.repository;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DbResource {
    private Long id;
    private String resourceId;
    private int type;
}
