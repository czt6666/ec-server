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

 Date: 03/12/2025 16:36:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for elderly_subject_mode_rel
-- ----------------------------
DROP TABLE IF EXISTS `elderly_subject_mode_rel`;
CREATE TABLE `elderly_subject_mode_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `subject_id` bigint NOT NULL COMMENT '主体ID（关联主体信息表）',
  `mode_id` bigint NOT NULL COMMENT '服务模式ID（关联服务模式表）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_subject_mode` (`subject_id`,`mode_id`) COMMENT '同一主体不能重复关联同一服务模式',
  KEY `idx_mode_id` (`mode_id`) COMMENT '服务模式索引，提升筛选效率',
  CONSTRAINT `fk_rel_mode` FOREIGN KEY (`mode_id`) REFERENCES `elderly_service_mode` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_rel_subject` FOREIGN KEY (`subject_id`) REFERENCES `elderly_service_subject` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='主体与服务模式关联表';

SET FOREIGN_KEY_CHECKS = 1;
