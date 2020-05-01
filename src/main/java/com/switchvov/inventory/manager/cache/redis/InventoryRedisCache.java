package com.switchvov.inventory.manager.cache.redis;

import com.switchvov.inventory.common.util.JsonUtil;
import com.switchvov.inventory.manager.cache.InventoryCache;
import com.switchvov.inventory.model.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryRedisCache.class);

    public static final String INVENTORY_KEY_PREFIX = "mall:inventory:";
    public static final long DEFAULT_TIMEOUT = 300;
    private ReactiveRedisTemplate<String, String> template;
    private ReactiveValueOperations<String, String> cache;

    public InventoryRedisCache(ReactiveRedisTemplate<String, String> template) {
        this.template = template;
        this.cache = this.template.opsForValue();
    }

    @Override
    public Mono<Boolean> put(Inventory inventory) {
        return put(inventory, DEFAULT_TIMEOUT);
    }

    @Override
    public Mono<Boolean> put(Inventory inventory, long timeout) {
        if (checkProductId(inventory.getProductId())) {
            return Mono.empty();
        }
        String key = getKey(inventory.getProductId());
        return cache.set(key, JsonUtil.serialization(inventory), Duration.ofSeconds(timeout));
    }

    @Override
    public Mono<Inventory> get(String productId) {
        if (checkProductId(productId)) {
            return Mono.empty();
        }
        String key = getKey(productId);
        return cache.get(key)
                .map(inventoryString -> JsonUtil.deserialization(inventoryString, Inventory.class));
    }

    @Override
    public Mono<Boolean> delete(String productId) {
        if (checkProductId(productId)) {
            return Mono.empty();
        }
        String key = getKey(productId);
        return cache.delete(key);
    }

    private String getKey(String productId) {
        return INVENTORY_KEY_PREFIX + productId;
    }

    private boolean checkProductId(String productId) {
        return StringUtils.isEmpty(productId);
    }
}
