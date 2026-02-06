-- 为 spu 表添加 shop_id 字段（关联店铺）
-- 执行时间：2026-01-30

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 添加 shop_id 字段
ALTER TABLE `spu`
    ADD COLUMN `shop_id` BIGINT NULL COMMENT '关联店铺ID' AFTER `user_id`;

-- 添加索引（可选，用于提高查询性能）
ALTER TABLE `spu`
    ADD INDEX `idx_shop_id` (`shop_id`);

SET FOREIGN_KEY_CHECKS = 1;
