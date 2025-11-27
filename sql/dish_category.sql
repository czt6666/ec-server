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

 Date: 25/11/2025 14:23:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dish_category
-- ----------------------------
DROP TABLE IF EXISTS `dish_category`;
CREATE TABLE `dish_category` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（分类唯一标识）',
  `restaurant_id` bigint unsigned NOT NULL COMMENT '关联门店ID（关联restaurant表的id）',
  `category_name` varchar(50) NOT NULL COMMENT '菜品分类名称（最多50字符）',
  `sort_num` int NOT NULL DEFAULT '0' COMMENT '排序号（拖拽调整后的值，数字越小越靠前）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_restaurant_name` (`restaurant_id`,`category_name`,`is_deleted`) COMMENT '同一门店未删除分类名称唯一',
  KEY `idx_restaurant_id` (`restaurant_id`) COMMENT '门店聚合查询索引',
  KEY `idx_sort_num` (`restaurant_id`,`sort_num`) COMMENT '门店内分类排序索引',
  KEY `idx_category_name` (`category_name`) COMMENT '分类名称搜索索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜品分类表（无分类编码）';

SET FOREIGN_KEY_CHECKS = 1;
