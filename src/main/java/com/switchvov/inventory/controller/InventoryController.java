package com.switchvov.inventory.controller;

import com.switchvov.inventory.common.http.BizResponse;
import com.switchvov.inventory.model.Inventory;
import com.switchvov.inventory.service.InventoryService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * 库存控制器
 *
 * @author switch
 * @since 2020/5/1
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public Mono<BizResponse<Inventory>> get(@PathVariable String productId) {
        Mono<Inventory> inventoryMono = inventoryService.getWithCache(productId);
        return inventoryMono.map(BizResponse::success);
    }

    @PutMapping("/{productId}")
    public Mono<BizResponse<Object>> update(@PathVariable String productId, @RequestParam("inventory") Long inventoryCount) {
        Inventory inventory = Inventory.builder().productId(productId).inventory(inventoryCount).build();
        inventoryService.updateWithCache(inventory);
        return Mono.fromCallable(BizResponse::success);
    }
}
