package com.switchvov.inventory.manager.cache;

import com.switchvov.inventory.model.Inventory;
import reactor.core.publisher.Mono;

/**
 * 库存缓存接口
 *
 * @author switch
 * @since 2020/5/1
 */
public interface InventoryCache {
    /**
     * 设置库存缓存
     *
     * @param inventory 库存对象
     * @return Mono包裹的设置结果
     */
    Mono<Boolean> put(Inventory inventory);

    /**
     * 设置库存缓存
     *
     * @param inventory 库存对象
     * @param timeout   超时时间
     * @return Mono包裹的设置结果
     */
    Mono<Boolean> put(Inventory inventory, long timeout);

    void putImmediately(Inventory inventory);

    void putImmediately(Inventory inventory, long timeout);

    /**
     * 获取库存缓存
     *
     * @param productId 商品ID
     * @return Mono包裹的库存对象
     */
    Mono<Inventory> get(String productId);

    /**
     * 立即获取库存缓存
     *
     * @param productId 商品ID
     * @return 库存对象
     */
    Inventory getImmediately(String productId);

    /**
     * 删除库存缓存
     *
     * @param productId 商品ID
     * @return Mono包裹的删除结果
     */
    Mono<Boolean> delete(String productId);

    Boolean deleteImmediately(String productId);
}
