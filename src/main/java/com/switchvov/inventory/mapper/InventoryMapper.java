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
     * @return
     */
    List<Inventory> listAll();
}
