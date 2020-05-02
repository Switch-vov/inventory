package com.switchvov.inventory.business;

import com.switchvov.inventory.model.Inventory;
import com.switchvov.inventory.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

/**
 * @author switch
 * @since 2020/5/2
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class InventoryServiceTests {
    @Autowired
    private InventoryService inventoryService;

    @Test
    public void testGetCache() {
        String productId = "abc";
        System.out.println(inventoryService.getWithCache(productId).block());
    }

    @Test
    public void testReadWriter() throws InterruptedException {
        Inventory inventory = Inventory.builder().inventory(-1L).productId("abc").build();
        for (int i = 0; i < 1; i++) {
            Inventory inventory1 = inventoryService.getWithCache(inventory.getProductId()).block();
            inventoryService.updateWithCache(inventory);
            TimeUnit.MILLISECONDS.sleep(500);
            Inventory inventory2 = inventoryService.getWithCache(inventory.getProductId()).block();
            System.out.println(inventory1.getId() + ":" + inventory1.getInventory() + " " + inventory2.getInventory());
        }
    }
}
