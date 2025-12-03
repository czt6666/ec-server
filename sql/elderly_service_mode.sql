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

 Date: 03/12/2025 16:35:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for elderly_service_mode
-- ----------------------------
DROP TABLE IF EXISTS `elderly_service_mode`;
CREATE TABLE `elderly_service_mode` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `mode_name` varchar(100) NOT NULL COMMENT '服务模式名称（机构住养/日间照料/上门服务/综合型）',
  `sort` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态（1：启用；0：禁用）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mode_name` (`mode_name`) COMMENT '服务模式名称唯一'
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='养老服务模式表';

SET FOREIGN_KEY_CHECKS = 1;
