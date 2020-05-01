package com.switchvov.inventory.service.impl;

import com.switchvov.inventory.manager.cache.InventoryCache;
import com.switchvov.inventory.mapper.InventoryMapper;
import com.switchvov.inventory.model.Inventory;
import com.switchvov.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author switch
 * @since 2020/5/1
 */
@Service("InventoryService")
public class InventoryServiceImpl implements InventoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private InventoryMapper inventoryMapper;
    private InventoryCache inventoryCache;

    public InventoryServiceImpl(InventoryMapper inventoryMapper, InventoryCache inventoryCache) {
        this.inventoryMapper = inventoryMapper;
        this.inventoryCache = inventoryCache;
    }

    @Override
    public Mono<Boolean> deleteCache(String productId) {
        return inventoryCache.delete(productId);
    }

    @Override
    public Mono<Integer> update(Inventory inventory) {
        return Mono.fromCallable(() -> inventoryMapper.update(inventory));
    }

    @Override
    public Mono<Inventory> get(String productId) {
        return Mono.fromCallable(() -> inventoryMapper.get(productId));
    }

    @Override
    public Mono<Boolean> updateCache(Inventory inventory) {
        return inventoryCache.put(inventory.getProductId(), inventory);
    }
}
