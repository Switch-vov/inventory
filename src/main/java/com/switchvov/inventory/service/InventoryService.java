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

    /**
     * 更新数据库
     *
     * @param inventory 库存对象
     * @return 更新影响条数
     */
    Mono<Integer> update(Inventory inventory);

    /**
     * 获取库存对象
     *
     * @param productId 商品ID
     * @return 库存对象
     */
    Mono<Inventory> get(String productId);

    /**
     * 更新缓存
     *
     * @param inventory 库存对象
     * @return 更新结果
     */
    Mono<Boolean> updateCache(Inventory inventory);
}
