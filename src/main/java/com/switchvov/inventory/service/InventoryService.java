package com.switchvov.inventory.service;

import com.switchvov.inventory.model.Inventory;
import reactor.core.publisher.Mono;

/**
 * 库存服务接口
 *
 * @author switch
 * @since 2020/5/1
 */
public interface InventoryService {
    /**
     * 删除缓存
     *
     * @param productId 商品ID
     * @return 删除结果
     */
    Mono<Boolean> deleteCache(String productId);

    Boolean deleteImmediately(String productId);

    /**
     * 更新数据库
     *
     * @param inventory 库存对象
     * @return 更新影响条数
     */
    Mono<Integer> update(Inventory inventory);

    Integer updateImmediately(Inventory inventory);

    /**
     * 获取库存对象
     *
     * @param productId 商品ID
     * @return 库存对象
     */
    Mono<Inventory> get(String productId);

    Inventory getImmediately(String productId);

    /**
     * 更新缓存
     *
     * @param inventory 库存对象
     * @return 更新结果
     */
    Mono<Boolean> updateCache(Inventory inventory);

    void updateCacheImmediately(Inventory inventory);

    /**
     * 基于缓存的库存对象获取
     *
     * @param productId 商品ID
     * @return 库存对象
     */
    Mono<Inventory> getWithCache(String productId);

    /**
     * 基于缓存的库存更新
     *
     * @param inventory 库存对象
     */
    void updateWithCache(Inventory inventory);
}
