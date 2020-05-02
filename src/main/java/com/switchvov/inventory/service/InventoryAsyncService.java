package com.switchvov.inventory.service;

import com.switchvov.inventory.manager.cache.request.Request;

/**
 * 库存异步处理服务
 *
 * @author switch
 * @since 2020/5/1
 */
public interface InventoryAsyncService {
    /**
     * 处理请求，路由到相应队列
     *
     * @param request 请求对象
     */
    void process(Request request);
}
