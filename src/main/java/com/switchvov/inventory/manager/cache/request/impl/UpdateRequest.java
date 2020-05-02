package com.switchvov.inventory.manager.cache.request.impl;

import com.switchvov.inventory.manager.cache.request.Request;
import com.switchvov.inventory.manager.cache.request.RequestType;
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
    private final InventoryService inventoryService;
    private final Inventory inventory;

    public UpdateRequest(InventoryService inventoryService, Inventory inventory) {
        this.inventoryService = inventoryService;
        this.inventory = inventory;
    }

    @Override
    public void process() {
        // 删除缓存，更新数据库
        inventoryService.deleteImmediately(inventory.getProductId());
        inventoryService.updateImmediately(inventory);
    }

    @Override
    public String getRouteKey() {
        return inventory.getProductId();
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.UPDATE;
    }

    @Override
    public boolean force() {
        return true;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "inventory=" + inventory +
                '}';
    }
}
