package com.switchvov.inventory.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON 工具类
 *
 * @author switch
 * @since 2020/4/30
 */
public final class JsonUtil {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 序列化
     *
     * @param obj 需要序列化的对象
     * @param <T> 对象类型
     * @return 序列后的JSON串
     */
    public static <T> String serialization(T obj) {
        String serial;
        try {
            serial = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("序列化错误：序列对象为{}，错误原因为：{}", obj.toString(), e.getMessage());
            serial = "";
        }
        return serial;
    }

    /**
     * 反序列化
     *
     * @param serial JSON串
     * @param clazz  对象的Class
     * @param <T>    对象类型
     * @return 对象
     */
    public static <T> T deserialization(String serial, Class<T> clazz) {
        T obj;
        try {
            obj = OBJECT_MAPPER.readValue(serial, clazz);
        } catch (JsonProcessingException e) {
            LOGGER.error("反序列化错误：字符为{}，错误原因为：{}", serial, e.getMessage());
            obj = null;
        }
        return obj;
    }
}
