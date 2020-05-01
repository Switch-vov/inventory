package com.switchvov.inventory.manager.cache.redis;

import com.switchvov.inventory.common.util.JsonUtil;
import com.switchvov.inventory.manager.cache.InventoryCache;
import com.switchvov.inventory.model.Inventory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 库存缓存Redis实现
 *
 * @author switch
 * @since 2020/4/30
 */
@Component
public class InventoryRedisCache implements InventoryCache {
    public static final String INVENTORY_KEY_PREFIX = "mall:inventory:";
    public static final long DEFAULT_TIMEOUT = 300;
    private ReactiveRedisTemplate<String, String> template;
    private ReactiveValueOperations<String, String> cache;

    public InventoryRedisCache(ReactiveRedisTemplate<String, String> template) {
        this.template = template;
        this.cache = this.template.opsForValue();
    }

    @Override
    public Mono<Boolean> put(String productId, Inventory inventory) {
        return put(productId, inventory, DEFAULT_TIMEOUT);
    }

    @Override
    public Mono<Boolean> put(String productId, Inventory inventory, long timeout) {
        if (checkProductId(productId)) {
            return Mono.empty();
        }
        String key = getKey(productId);
        return cache.set(key, JsonUtil.serialization(inventory), Duration.ofSeconds(timeout));
    }

    @Override
    public Mono<Inventory> get(String productId) {
        if (checkProductId(productId)) {
            return null;
        }
        String key = getKey(productId);
        return Mono.fromCallable(() -> JsonUtil.deserialization(cache.get(key).block(), Inventory.class));
    }

    private String getKey(String productId) {
        return INVENTORY_KEY_PREFIX + productId;
    }

    private boolean checkProductId(String productId) {
        return StringUtils.isEmpty(productId);
    }
}
