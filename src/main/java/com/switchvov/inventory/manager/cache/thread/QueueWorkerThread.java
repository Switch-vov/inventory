package com.switchvov.inventory.manager.cache.thread;

import com.switchvov.inventory.manager.cache.queue.RequestQueue;
import com.switchvov.inventory.manager.cache.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * <p>工作线程</p>
 *
 * <p>实现{@link Callable}接口，泛型为{@link Boolean}类型，只是为了标识执行情况</p>
 *
 * @author switch
 * @since 2020/5/1
 */
public class QueueWorkerThread implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueWorkerThread.class);
    private RequestQueue requestQueue;

    public QueueWorkerThread(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public Boolean call() throws Exception {
        while (true) {
            Request request = null;
            try {
                request = requestQueue.take();
                request.process();
            } catch (Exception e) {
                LOGGER.error("队列工作线程出错，队列ID为：{}，请求对象为：{}，错误为：{}", requestQueue.getQueueId(), request, e.getMessage());
            }
        }
    }
}
