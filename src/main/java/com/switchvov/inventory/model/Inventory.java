package com.switchvov.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author switch
 * @since 2020/4/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {
    @Id
    private Long id;
    private String productId;
    private Long inventory;
    private Date created;
    private Date updated;
}
