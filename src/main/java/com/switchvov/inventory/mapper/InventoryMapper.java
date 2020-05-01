package com.switchvov.inventory.mapper;

import com.switchvov.inventory.model.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author switch
 * @since 2020/4/30
 */
@Repository
@Mapper
public interface InventoryMapper {
    /**
     * 获取
     *
     * @return
     */
    List<Inventory> listAll();

    /**
     * 更新库存
     *
     * @param inventory 库存对象
     * @return 更新影响条数
     */
    int update(Inventory inventory);

    /**
     * 获取库存对象
     *
     * @param productId 商品ID
     * @return 库存对象
     */
    Inventory get(String productId);
}
