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

 Date: 30/01/2026 23:26:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单详情ID',
  `order_id` bigint NOT NULL COMMENT '订单ID（关联order_info表）',
  `dish_id` bigint NOT NULL COMMENT '菜品ID（关联dish表）',
  `dish_name` varchar(100) NOT NULL COMMENT '菜品名称（下单时的菜品名称快照）',
  `dish_price` decimal(10,2) NOT NULL COMMENT '菜品单价（下单时的价格快照）',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '数量',
  `subtotal_amount` decimal(10,2) NOT NULL COMMENT '小计金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_dish_id` (`dish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单详情表';

SET FOREIGN_KEY_CHECKS = 1;
