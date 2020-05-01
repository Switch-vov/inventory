package com.switchvov.inventory.manager.cache.request.impl;


import com.switchvov.inventory.manager.cache.request.Request;
import com.switchvov.inventory.service.InventoryService;

/**
 * <p>刷新缓存请求</p>
 * <p>查数据库并设置到缓存</p>
 *
 * @author switch
 * @since 2020/5/1
 */
public class RefreshRequest implements Request {
    private String productId;
    private InventoryService inventoryService;

    public RefreshRequest(InventoryService inventoryService, String productId) {
        this.productId = productId;
        this.inventoryService = inventoryService;
    }

    @Override
    public void process() {
        // 查数据库并设置到缓存
        inventoryService.get(productId)
                .flatMap(inventory -> inventoryService.updateCache(inventory))
                .block();
    }
}
