-- 店铺管理表

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '店铺ID',
                        `shop_name` varchar(100) NOT NULL COMMENT '店铺名称',
                        `shop_abbreviation` varchar(50) DEFAULT NULL COMMENT '店铺缩写（用于生成默认用户名）',
                        `product_type` varchar(50) DEFAULT NULL COMMENT '产品类型',
                        `business_status` tinyint DEFAULT '1' COMMENT '经营状态：0-停业，1-营业',
                        `village` varchar(100) DEFAULT NULL COMMENT '所属村',
                        `shop_intro` text COMMENT '店铺简介',
                        `shop_avatar` varchar(255) DEFAULT NULL COMMENT '店铺头像',
                        `shop_address` varchar(255) DEFAULT NULL COMMENT '店铺地址',
                        `qualification_files` text COMMENT '资质凭证（JSON格式存储多个文件路径，如：["/uploads/license1.jpg", "/uploads/food_license.jpg"]）',
                        `user_id` bigint DEFAULT NULL COMMENT '关联的用户ID（商家账号）',
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_user_id` (`user_id`),
                        KEY `idx_business_status` (`business_status`),
                        KEY `idx_village` (`village`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='店铺表';

SET FOREIGN_KEY_CHECKS = 1;
ALTER TABLE shop ADD COLUMN display_no INT NULL COMMENT '展示顺序号';

SET @rn := 0;
UPDATE shop SET display_no = (@rn := @rn + 1) ORDER BY create_time;
