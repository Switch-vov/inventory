package com.switchvov.inventory.manager.cache.request.impl;

import com.switchvov.inventory.manager.cache.request.Request;
import com.switchvov.inventory.model.Inventory;
import com.switchvov.inventory.service.InventoryService;

/**
 * <p>更新请求</p>
 * <p>cache aside模式</p>
 * <p>先删缓存，再更新数据库</p>
 *
 * @author switch
 * @since 2020/5/1
 */
public class UpdateRequest implements Request {
    private InventoryService inventoryService;
    private Inventory inventory;

    public UpdateRequest(InventoryService inventoryService, Inventory inventory) {
        this.inventoryService = inventoryService;
        this.inventory = inventory;
    }

    @Override
    public void process() {
        // 删除缓存，更新数据库
        inventoryService.deleteCache(inventory.getProductId())
                .flatMap(deleteResult -> inventoryService.update(inventory))
                .block();
    }
}
