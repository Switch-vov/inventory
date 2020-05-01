package com.switchvov.inventory.manager.cache.thread;

import com.switchvov.inventory.manager.cache.queue.RequestQueue;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * 队列线程池
 *
 * @author switch
 * @since 2020/5/1
 */
public class QueueThreadPool {
    public static final int DEFAULT_QUEUE_SIZE = 100;

    /**
     * 线程池
     */
    private ThreadPoolTaskExecutor executor;
    /**
     * 队列大小
     */
    private int queueSize;
    /**
     * 队列列表
     */
    private List<RequestQueue> queues;

    public QueueThreadPool(ThreadPoolTaskExecutor executor) {
        this(executor, DEFAULT_QUEUE_SIZE);
    }

    public QueueThreadPool(ThreadPoolTaskExecutor executor, int queueSize) {
        this.executor = executor;
        this.queueSize = queueSize;
        this.queues = new ArrayList<>(getPoolSize());
    }

    /**
     * 初始化{@link QueueThreadPool#getPoolSize()}个队列，并将每条队列绑定至某一线程
     */
    public void init() {
        int poolSize = getPoolSize();
        for (int i = 0; i < poolSize; i++) {
            RequestQueue requestQueue = new RequestQueue(i, queueSize);
            QueueWorkerThread workerThread = new QueueWorkerThread(requestQueue);
            executor.submit(workerThread);
        }
    }

    public int getPoolSize() {
        return executor.getPoolSize();
    }

    public List<RequestQueue> getQueues() {
        return queues;
    }

    @Override
    public String toString() {
        return "QueueThreadPool{" +
                "executor=" + executor +
                ", queueSize=" + queueSize +
                ", queues=" + queues +
                '}';
    }
}
