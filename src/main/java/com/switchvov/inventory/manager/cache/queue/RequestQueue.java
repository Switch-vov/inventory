package com.switchvov.inventory.manager.cache.queue;

import com.switchvov.inventory.manager.cache.request.Request;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 请求队列
 *
 * @author switch
 * @since 2020/5/1
 */
public class RequestQueue {
    private int queueId;
    private ArrayBlockingQueue<Request> queue;

    public RequestQueue(int queueId, int capacity) {
        this.queueId = queueId;
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    /**
     * 放入request，当队列为满是阻塞
     *
     * @param request 请求对象
     */
    public void put(Request request) throws InterruptedException {
        queue.put(request);
    }

    /**
     * 获取request，当队列为空时阻塞
     *
     * @return 请求对象
     */
    public Request take() throws InterruptedException {
        return queue.take();
    }

    public int getQueueId() {
        return queueId;
    }

    @Override
    public String toString() {
        return "RequestQueue{" +
                "queueId=" + queueId +
                ", queue=" + queue +
                '}';
    }
}
