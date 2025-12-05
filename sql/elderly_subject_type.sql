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

 Date: 03/12/2025 16:35:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for elderly_subject_type
-- ----------------------------
DROP TABLE IF EXISTS `elderly_subject_type`;
CREATE TABLE `elderly_subject_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_name` varchar(100) NOT NULL COMMENT '主体类型名称（养老机构/社区养老/居家养老/老年医院/养老服务中心）',
  `sort` int DEFAULT '0' COMMENT '排序（数字越小越靠前）',
  `status` tinyint DEFAULT '1' COMMENT '状态（1：启用；0：禁用）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_type_name` (`type_name`) COMMENT '类型名称唯一，避免重复'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='养老服务主体类型表';

SET FOREIGN_KEY_CHECKS = 1;
