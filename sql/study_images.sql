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

 Date: 02/02/2026 16:11:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for study_images
-- ----------------------------
DROP TABLE IF EXISTS `study_images`;
CREATE TABLE `study_images` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `related_id` bigint NOT NULL COMMENT '关联对象ID（研学基地ID或研学活动ID）',
  `related_type` varchar(50) NOT NULL COMMENT '关联类型（base：研学基地，activity：研学活动，plan：研学方案）',
  `image_url` varchar(500) NOT NULL COMMENT '图片URL地址',
  `image_name` varchar(200) DEFAULT NULL COMMENT '图片原始名称',
  `sort_order` int DEFAULT '0' COMMENT '排序序号（数值越小越靠前）',
  `is_cover` tinyint DEFAULT '0' COMMENT '是否封面图（0：否，1：是）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_related_id` (`related_id`) COMMENT '关联对象ID索引',
  KEY `idx_related_type` (`related_type`) COMMENT '关联类型索引',
  KEY `idx_sort_order` (`sort_order`) COMMENT '排序索引',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引'
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='研学模块图片表';

SET FOREIGN_KEY_CHECKS = 1;
