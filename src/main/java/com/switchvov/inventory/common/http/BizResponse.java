package com.switchvov.inventory.common.http;

/**
 * 业务通用响应类
 *
 * @author switch
 * @since 2020/5/1
 */
public class BizResponse<T> {
    public static final int SUCCESS_CODE = 0;
    public static final boolean SUCCESS = true;

    private int code;
    private boolean success;
    private T data;

    protected BizResponse(T data) {
        this.code = SUCCESS_CODE;
        this.success = SUCCESS;
        this.data = data;
    }

    protected BizResponse(int code, boolean success, T data) {
        this.code = code;
        this.success = success;
        this.data = data;
    }

    public static <T> BizResponse<T> success() {
        return success(null);
    }

    public static <T> BizResponse<T> success(T data) {
        return new BizResponse<>(data);
    }

    public static <T> BizResponse<T> response(int code, boolean success, T data) {
        return new BizResponse<>(code, success, data);
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}
