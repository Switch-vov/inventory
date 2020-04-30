package com.switchvov.inventory.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author switch
 * @since 2020/4/30
 */
@Data
public class Inventory {
    @Id
    private Long id;
    private String productId;
    private Long inventory;
    private Date created;
    private Date updated;
}
