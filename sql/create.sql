CREATE TABLE `inventory`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_id` char(32) NOT NULL DEFAULT '' COMMENT '商品ID',
    `inventory`  bigint   NOT NULL DEFAULT '0' COMMENT '库存',
    `created`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated`    datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_product_id` (`product_id`) USING BTREE COMMENT '商品ID索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='库存表';