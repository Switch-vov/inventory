package com.switchvov.inventory.manager.cache.thread;

import com.switchvov.inventory.manager.cache.queue.RequestQueue;
import com.switchvov.inventory.manager.cache.request.Request;
import com.switchvov.inventory.manager.cache.request.RequestType;
import com.switchvov.inventory.manager.cache.request.impl.RefreshRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final RequestQueue requestQueue;

    private final AtomicInteger runCnt;

    public QueueWorkerThread(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        this.runCnt = new AtomicInteger(0);
    }

    @Override
    public Boolean call() throws Exception {
        Request request = null;
        while (true) {
            try {
                request = requestQueue.take();
                boolean checkRequest = checkRequest(request);
                int runCount = runCnt.incrementAndGet();
                LOGGER.debug("线程ID：{}，队列ID：{}，执行序号：{}，请求：{}，是否执行：{}", Thread.currentThread().getId(),
                        requestQueue.getQueueId(), runCount, request, checkRequest);
                if (checkRequest) {
                    request.process();
                }
            } catch (Exception e) {
                LOGGER.error("队列工作线程出错，队列ID为：{}，请求对象为：{}，错误为：{}", requestQueue.getQueueId(), request, e.getMessage());
            }
        }
    }

    /**
     * 是否执行处理
     *
     * @param request
     * @return
     */
    private boolean checkRequest(Request request) {
        // 写请求不区分直接执行；读请求队列中有，只需执行一次
        if (request.force()) {
            return true;
        }
        if (request instanceof RefreshRequest) {
            String mappingKey = requestQueue.getMappingKey(request);
            String writeMappingKey = requestQueue.getMappingKey(request.getRouteKey(), RequestType.UPDATE);
            Integer readRequestCount = requestQueue.getRequestCount(mappingKey);
            Integer writeRequestCount = requestQueue.getRequestCount(writeMappingKey);
            LOGGER.debug("路由ID：{}，读请求数量：{}，写请求数量：{}", request.getRouteKey(), readRequestCount, writeRequestCount);
            // 该队列中没有写请求，跳过执行
            if (Objects.isNull(writeRequestCount) || writeRequestCount <= 0) {
                return false;
            }
            // 该队列中有读请求，且请求不止一个时，跳过执行
            if (requestQueue.containRequestCount(mappingKey) && readRequestCount > 0) {
                return false;
            }
        }
        return true;
    }
}
