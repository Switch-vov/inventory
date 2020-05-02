package com.switchvov.inventory.manager.cache.request;

/**
 * 请求类型
 *
 * @author switch
 * @since 2020/5/2
 */
public enum RequestType {
    /**
     * 更新
     */
    UPDATE(1),
    /**
     * 读取
     */
    READ(2);

    /**
     * 类型值
     */
    private final int typeValue;

    RequestType(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return typeValue;
    }
}
