package com.switchvov.inventory.manager.cache.request;

/**
 * 请求接口
 *
 * @author switch
 * @since 2020/5/1
 */
public interface Request {
    /**
     * 逻辑处理方法
     */
    void process();

    /**
     * 获取路由ID
     *
     * @return 路由ID
     */
    String getRouteKey();

    /**
     * 获取请求类型
     *
     * @return 请求类型
     */
    RequestType getRequestType();

    /**
     * 是否强制执行
     *
     * @return
     */
    boolean force();
}
