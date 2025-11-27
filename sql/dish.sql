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

 Date: 25/11/2025 14:36:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（菜品唯一标识）',
  `restaurant_id` bigint unsigned NOT NULL COMMENT '关联门店ID（关联restaurant表的id）',
  `category_id` bigint unsigned NOT NULL COMMENT '关联菜品分类ID（关联dish_category表的id）',
  `dish_name` varchar(100) NOT NULL COMMENT '菜品名称（最多100字符）',
  `cover_img_url` varchar(255) DEFAULT '' COMMENT '菜品封面图（仅1张，存储图片地址）',
  `dish_status` tinyint NOT NULL DEFAULT '1' COMMENT '菜品状态：1=上架，0=下架',
  `price` decimal(5,2) NOT NULL COMMENT '菜品售价（0-999.99，保留2位小数）',
  `unit` varchar(5) DEFAULT '' COMMENT '单位（最多5字符，如“份”“个”）',
  `summary` varchar(30) DEFAULT '' COMMENT '菜品概要（最多30字符）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除：0=未删除，1=已删除',
  PRIMARY KEY (`id`),
  KEY `idx_restaurant_id` (`restaurant_id`) COMMENT '按门店筛选菜品索引',
  KEY `idx_category_id` (`category_id`) COMMENT '按分类筛选菜品索引',
  KEY `idx_dish_name` (`dish_name`) COMMENT '菜品名称搜索索引',
  KEY `idx_dish_status` (`dish_status`) COMMENT '按上下架状态筛选索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜品信息表';

SET FOREIGN_KEY_CHECKS = 1;

-- 给dish表添加sort_num字段
ALTER TABLE `dish` 
ADD COLUMN `sort_num` INT NOT NULL DEFAULT 0 COMMENT '排序号' AFTER `summary`;

-- 为sort_num字段创建索引，提高排序查询效率
ALTER TABLE `dish` 
ADD INDEX `idx_sort_num` (`sort_num`) COMMENT '排序号索引';