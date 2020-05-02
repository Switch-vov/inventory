package com.switchvov.inventory.service.impl;

import com.switchvov.inventory.manager.cache.queue.RequestQueue;
import com.switchvov.inventory.manager.cache.request.Request;
import com.switchvov.inventory.manager.cache.thread.QueueThreadPool;
import com.switchvov.inventory.service.InventoryAsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author switch
 * @since 2020/5/1
 */
@Service("inventoryAsyncService")
public class InventoryAsyncServiceImpl implements InventoryAsyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryAsyncServiceImpl.class);

    private final ThreadPoolTaskExecutor requestExecutor;
    private final QueueThreadPool queueThreadPool;
    private final List<RequestQueue> queues;


    public InventoryAsyncServiceImpl(ThreadPoolTaskExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
        this.queueThreadPool = new QueueThreadPool(this.requestExecutor);
        this.queueThreadPool.init();
        this.queues = this.queueThreadPool.getQueues();
    }

    @Override
    public void process(Request request) {
        // 做请求的路由，根据每个请求的商品id，路由到对应的内存队列中去
        RequestQueue queue = getRoutingQueue(request.getRouteKey());
        try {
            // 将请求放入对应的队列中，完成路由操作
            queue.put(request);
            LOGGER.debug("路由队列为：{}，请求对象为：{}", queue.getQueueId(), request);
        } catch (Exception e) {
            LOGGER.error("路由错误，路由队列为：{}，请求对象为：{}，错误为：{}", queue.getQueueId(), request, e.getMessage());
            // TODO: 换成更具意义的异常抛出
            throw new RuntimeException("路由错误", e);
        }
    }

    /**
     * 获取路由到的内存队列
     *
     * @param routeId 路由ID
     * @return 内存队列
     */
    private RequestQueue getRoutingQueue(String routeId) {
        // 先获取productId的hash值
        int h;
        int hash = (routeId == null) ? 0 : (h = routeId.hashCode()) ^ (h >>> 16);

        // 对hash值取模，将hash值路由到指定的内存队列中，比如内存队列大小8
        // 用内存队列的数量对hash值取模之后，结果一定是在0~7之间
        // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
        int index = (queues.size() - 1) & hash;
        return queues.get(index);
    }
}
