package com.switchvov.inventory.manager.cache.queue;

import com.switchvov.inventory.manager.cache.request.Request;
import com.switchvov.inventory.manager.cache.request.RequestType;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求队列
 *
 * @author switch
 * @since 2020/5/1
 */
public class RequestQueue {
    private final int queueId;
    private final ArrayBlockingQueue<Request> queue;
    private final Map<String, Integer> requestMapping;

    public RequestQueue(int queueId, int capacity) {
        this.queueId = queueId;
        this.queue = new ArrayBlockingQueue<>(capacity);
        this.requestMapping = new ConcurrentHashMap<>();
    }

    /**
     * 放入request，当队列为满是阻塞
     *
     * @param request 请求对象
     */
    public void put(Request request) throws InterruptedException {
        queue.put(request);
        // TODO：可以优化成基于key的锁，减少锁的粒度
        synchronized (requestMapping) {
            String key = getMappingKey(request);
            requestMapping.put(key, requestMapping.getOrDefault(key, 0) + 1);
        }
    }

    /**
     * 获取request，当队列为空时阻塞
     *
     * @return 请求对象
     */
    public Request take() throws InterruptedException {
        Request request = queue.take();
        // TODO：可以优化成基于key的锁，减少锁的粒度
        synchronized (requestMapping) {
            String key = getMappingKey(request);
            requestMapping.computeIfPresent(key, (k, v) -> v < 0 ? 0 : v - 1);
        }
        return request;
    }

    /**
     * 获取队列ID
     *
     * @return 队列ID
     */
    public int getQueueId() {
        return queueId;
    }

    /**
     * 获取MappingKey
     *
     * @param request 请求对象
     * @return MappingKey
     */
    public String getMappingKey(Request request) {
        return getMappingKey(request.getRouteKey(), request.getRequestType());
    }

    /**
     * 获取MappingKey
     *
     * @param routeKey    路由Key
     * @param requestType 请求类型
     * @return MappingKey
     */
    public String getMappingKey(String routeKey, RequestType requestType) {
        return routeKey + '-' + requestType.getTypeValue();
    }

    public Integer getRequestCount(String mappingKey) {
        return requestMapping.get(mappingKey);
    }

    public boolean containRequestCount(String mappingKey) {
        return requestMapping.containsKey(mappingKey);
    }

    @Override
    public String toString() {
        return "RequestQueue{" +
                "queueId=" + queueId +
                ", queue=" + queue +
                '}';
    }
}
