package com.switchvov.inventory.service.impl;

import com.switchvov.inventory.manager.cache.InventoryCache;
import com.switchvov.inventory.manager.cache.request.impl.RefreshRequest;
import com.switchvov.inventory.manager.cache.request.impl.UpdateRequest;
import com.switchvov.inventory.mapper.InventoryMapper;
import com.switchvov.inventory.model.Inventory;
import com.switchvov.inventory.service.InventoryAsyncService;
import com.switchvov.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author switch
 * @since 2020/5/1
 */
@Service("InventoryService")
public class InventoryServiceImpl implements InventoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private static final long MAX_WAIT_TIME = 200;
    private static final long MAX_PHASE_TIME = 20;

    private final InventoryMapper inventoryMapper;
    private final InventoryCache inventoryCache;
    private final InventoryAsyncService inventoryAsyncService;
    private final long maxWaitTime = MAX_WAIT_TIME;
    private final long maxPhaseTime = MAX_PHASE_TIME;

    public InventoryServiceImpl(InventoryMapper inventoryMapper, InventoryCache inventoryCache, InventoryAsyncService inventoryAsyncService) {
        this.inventoryMapper = inventoryMapper;
        this.inventoryCache = inventoryCache;
        this.inventoryAsyncService = inventoryAsyncService;
    }

    @Override
    public Mono<Inventory> get(String productId) {
        return Mono.fromCallable(() -> inventoryMapper.get(productId));
    }

    @Override
    public Inventory getImmediately(String productId) {
        return inventoryMapper.get(productId);
    }

    @Override
    public Mono<Integer> update(Inventory inventory) {
        return Mono.fromCallable(() -> inventoryMapper.update(inventory));
    }

    @Override
    public Integer updateImmediately(Inventory inventory) {
        return inventoryMapper.update(inventory);
    }

    public Mono<Inventory> getCache(String productId) {
        return inventoryCache.get(productId);
    }

    public Inventory getCacheImmediately(String productId) {
        return inventoryCache.getImmediately(productId);
    }

    @Override
    public Mono<Boolean> updateCache(Inventory inventory) {
        return inventoryCache.put(inventory);
    }

    @Override
    public void updateCacheImmediately(Inventory inventory) {
        inventoryCache.putImmediately(inventory);
    }

    @Override
    public Mono<Boolean> deleteCache(String productId) {
        return inventoryCache.delete(productId);
    }

    @Override
    public Boolean deleteImmediately(String productId) {
        return inventoryCache.deleteImmediately(productId);
    }

    @Override
    public Mono<Inventory> getWithCache(String productId) {
        // 创建刷新请求
        RefreshRequest request = new RefreshRequest(this, productId);
        // 处理刷新请求
        inventoryAsyncService.process(request);
        return getWait(productId);
    }

    private Mono<Inventory> getWait(String productId) {
        long waitTime = 0L;
        long startTime = System.currentTimeMillis();
        long endTime;
        try {
            // 在maxWaitTime的时间内，不断的查询缓存
            while (waitTime <= maxWaitTime) {
                // 尝试去redis中读取商品库存的缓存数据
                Inventory inventory = getCacheImmediately(productId);
                LOGGER.debug("尝试从缓存中读取库存信息：{}，当前等待毫秒数：{}", inventory, waitTime);
                if (inventory != null) {
                    return Mono.fromCallable(() -> inventory);
                } else {
                    TimeUnit.MILLISECONDS.sleep(maxPhaseTime);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime - startTime;
                }
            }

            // 尝试从数据库中读取数据
            Inventory inventory = getImmediately(productId);
            LOGGER.debug("尝试从数据库中读取库存信息：{}", inventory);
            // TODO：这块会出现缓存穿透问题，也就是大量的查询，其查询的商品不在数据库中。这块问题可以通过前置的布隆过滤器解决
            if (inventory != null) {
                // 创建强制刷新请求
                RefreshRequest request = new RefreshRequest(this, productId, true);
                inventoryAsyncService.process(request);
                return Mono.fromCallable(() -> inventory);
            }
        } catch (Exception e) {
            LOGGER.error("获取商品：{}，库存错误，原因为：{}", productId, e.getMessage());
        }
        return Mono.empty();
    }

    @Override
    public void updateWithCache(Inventory inventory) {
        // 创建更新请求对象
        UpdateRequest request = new UpdateRequest(this, inventory);
        // 处理更新请求
        inventoryAsyncService.process(request);
    }
}
