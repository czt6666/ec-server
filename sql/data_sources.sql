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

 Date: 12/12/2025 00:57:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_sources
-- ----------------------------
DROP TABLE IF EXISTS `data_sources`;
CREATE TABLE `data_sources` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '业务模块名称',
  `table_name` varchar(100) NOT NULL COMMENT '对应的数据表名',
  `status` tinyint DEFAULT '1' COMMENT '状态(0-禁用, 1-启用)',
  `sync_frequency` varchar(50) DEFAULT '0 0 * * * ?' COMMENT '同步频率(cron表达式)',
  `last_sync_time` datetime DEFAULT NULL COMMENT '最后同步时间',
  `record_count` int DEFAULT '0' COMMENT '记录数',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
