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

 Date: 26/03/2026 14:52:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for restaurant
-- ----------------------------
DROP TABLE IF EXISTS `restaurant`;
CREATE TABLE `restaurant` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '关联商家/管理员用户ID',
  `village_id` int NOT NULL COMMENT '所属村ID',
  `name` varchar(100) NOT NULL COMMENT '门店名称（≤100字符）',
  `business_start_time` char(5) NOT NULL COMMENT '开始营业时间 HH:mm',
  `business_end_time` char(5) NOT NULL COMMENT '结束营业时间 HH:mm',
  `logo_url` varchar(255) DEFAULT NULL COMMENT '门店Logo（单张图片URL）',
  `address` varchar(200) NOT NULL COMMENT '门店地址（≤200字符）',
  `coordinate_lat` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `coordinate_lng` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话（≤20字符）',
  `notice` varchar(500) DEFAULT NULL COMMENT '门店公告（≤500字符）',
  `license_urls` text COMMENT '证照图片JSON，含营业执照/食品许可证，单类≤15张',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1-正常，0-停用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sort_order` int DEFAULT '0' COMMENT '排序字段',
  PRIMARY KEY (`id`),
  KEY `idx_restaurant_user` (`user_id`),
  KEY `idx_restaurant_village` (`village_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='餐饮商铺表';

SET FOREIGN_KEY_CHECKS = 1;
