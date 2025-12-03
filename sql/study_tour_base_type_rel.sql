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

 Date: 03/12/2025 17:41:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for study_tour_base_type_rel
-- ----------------------------
DROP TABLE IF EXISTS `study_tour_base_type_rel`;
CREATE TABLE `study_tour_base_type_rel` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `base_id` bigint NOT NULL COMMENT '基地ID（关联研学基地表）',
  `type_id` bigint NOT NULL COMMENT '研学类型ID（关联研学类型表）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_base_type` (`base_id`,`type_id`) COMMENT '同一基地不能重复关联同一类型',
  KEY `idx_type_id` (`type_id`) COMMENT '研学类型索引，提升筛选效率',
  CONSTRAINT `fk_rel_base` FOREIGN KEY (`base_id`) REFERENCES `study_tour_base` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_rel_type` FOREIGN KEY (`type_id`) REFERENCES `study_tour_type` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='基地与研学类型关联表';

SET FOREIGN_KEY_CHECKS = 1;
