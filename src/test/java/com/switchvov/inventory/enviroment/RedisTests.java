package com.switchvov.inventory.enviroment;

import com.switchvov.inventory.manager.cache.redis.InventoryRedisCache;
import com.switchvov.inventory.mapper.InventoryMapper;
import com.switchvov.inventory.model.Inventory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author switch
 * @since 2020/4/30
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RedisTests {
    @Autowired
    private InventoryRedisCache inventoryCache;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Test
    public void testRedis() {
        List<Inventory> inventories = inventoryMapper.listAll();
        inventories.forEach(inventory -> inventoryCache.put(inventory).block());
        List<Inventory> selectInventories = inventories.stream()
                .map(Inventory::getProductId)
                .map(inventoryCache::get)
                .map(Mono::block)
                .collect(Collectors.toList());
        Assertions.assertEquals(inventories.size(), selectInventories.size());
    }
}
