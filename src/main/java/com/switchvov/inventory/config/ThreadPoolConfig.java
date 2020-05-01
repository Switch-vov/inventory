package com.switchvov.inventory.config;

import com.switchvov.inventory.common.util.ThreadPoolUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author switch
 * @since 2020/5/1
 */
@Configuration
public class ThreadPoolConfig {
    /**
     * <p>计算规则：N(thread) = N(cpu) * U(cpu) * (1 + w/c)</p>
     * <p>N(thread)：线程池大小</p>
     * <p>N(cpu)：处理器核数</p>
     * <p>U(cpu)：期望CPU利用率（该值应该介于0和1之间）</p>
     * <p>w/c：是等待时间与计算时间的比率，比如说IO操作即为等待时间，计算处理即为计算时间</p>
     */
    public static final int TASK_POOL_SIZE = 50;
    public static final int TASK_MAX_POOL_SIZE = 100;
    public static final int TASK_QUEUE_CAPACITY = 1000;

    public static final int REQUEST_POOL_SIZE = 50;
    public static final int REQUEST_QUEUE_CAPACITY = 1000;

    /**
     * 任务执行器，用于通用的异步任务处理
     *
     * @return 线程池
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        return ThreadPoolUtil.getDefaultExecutor(TASK_POOL_SIZE, TASK_MAX_POOL_SIZE, TASK_QUEUE_CAPACITY);
    }

    /**
     * 请求执行器，用于实现库存更新同步队列处理
     *
     * @return 线程池
     */
    @Bean
    public ThreadPoolTaskExecutor requestExecutor() {
        return ThreadPoolUtil.getDefaultExecutor(REQUEST_POOL_SIZE, REQUEST_POOL_SIZE, REQUEST_QUEUE_CAPACITY);
    }
}

