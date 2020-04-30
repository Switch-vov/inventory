package com.switchvov.inventory.manager.cache;

import com.switchvov.inventory.common.util.JsonUtil;
import com.switchvov.inventory.model.Inventory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author switch
 * @since 2020/4/30
 */
@Component
public class InventoryCache {
    public static final String INVENTORY_KEY_PREFIX = "mall:inventory:";
    public static final long DEFAULT_TIMEOUT = 300;
    private ReactiveRedisTemplate<String, String> template;
    private ReactiveValueOperations<String, String> cache;

    public InventoryCache(ReactiveRedisTemplate<String, String> template) {
        this.template = template;
        this.cache = this.template.opsForValue();
    }

    /**
     * 设置缓存
     *
     * @param productId 商品ID
     * @param inventory 库存对象
     * @return Mono包裹的设置结果
     */
    public Mono<Boolean> put(String productId, Inventory inventory) {
        return put(productId, inventory, DEFAULT_TIMEOUT);
    }

    /**
     * 设置库存缓存
     *
     * @param productId 商品ID
     * @param inventory 库存对象
     * @param timeout   超时时间
     * @return Mono包裹的设置结果
     */
    public Mono<Boolean> put(String productId, Inventory inventory, long timeout) {
        if (checkProductId(productId)) {
            return Mono.empty();
        }
        String key = getKey(productId);
        return cache.set(key, JsonUtil.serialization(inventory), Duration.ofSeconds(timeout));
    }

    /**
     * 获取库存缓存
     *
     * @param productId 商品ID
     * @return 库存
     */
    public Inventory get(String productId) {
        if (checkProductId(productId)) {
            return null;
        }
        String key = getKey(productId);
        return JsonUtil.deserialization(cache.get(key).block(), Inventory.class);
    }

    private String getKey(String productId) {
        return INVENTORY_KEY_PREFIX + productId;
    }

    private boolean checkProductId(String productId) {
        return StringUtils.isEmpty(productId);
    }
}
