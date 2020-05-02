package com.switchvov.inventory.manager.cache.request.impl;


import com.switchvov.inventory.manager.cache.request.Request;
import com.switchvov.inventory.manager.cache.request.RequestType;
import com.switchvov.inventory.model.Inventory;
import com.switchvov.inventory.service.InventoryService;

/**
 * <p>刷新缓存请求</p>
 * <p>查数据库并设置到缓存</p>
 *
 * @author switch
 * @since 2020/5/1
 */
public class RefreshRequest implements Request {
    private final String productId;
    private final InventoryService inventoryService;
    private boolean force;

    public RefreshRequest(InventoryService inventoryService, String productId) {
        this(inventoryService, productId, false);
    }

    public RefreshRequest(InventoryService inventoryService, String productId, boolean force) {
        this.productId = productId;
        this.inventoryService = inventoryService;
        this.force = force;
    }

    @Override
    public void process() {
        // 查数据库并设置到缓存
        Inventory inventory = inventoryService.getImmediately(productId);
        inventoryService.updateCacheImmediately(inventory);
    }

    @Override
    public String getRouteKey() {
        return productId;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.READ;
    }

    @Override
    public boolean force() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return "RefreshRequest{" +
                "productId='" + productId + '\'' +
                ", force=" + force +
                '}';
    }
}
