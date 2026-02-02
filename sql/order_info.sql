/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : ecadmin

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 30/01/2026 22:32:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号（唯一标识）',
  `user_id` bigint NOT NULL COMMENT '下单用户ID',
  `restaurant_id` bigint NOT NULL COMMENT '餐厅ID',
  `order_status` tinyint NOT NULL DEFAULT '1' COMMENT '订单状态：1-待处理，2-已确认，3-已完成，4-已取消',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '订单备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_restaurant_id` (`restaurant_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_order_status` (`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单主表';

SET FOREIGN_KEY_CHECKS = 1;
