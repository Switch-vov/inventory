package com.switchvov.inventory.enviroment;

import com.switchvov.inventory.mapper.InventoryMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author switch
 * @since 2020/4/30
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MyBatisTests {
    @Autowired
    private InventoryMapper inventoryMapper;

    @Test
    public void testMyBatis() {
        inventoryMapper.listAll().forEach(System.out::println);
    }
}
