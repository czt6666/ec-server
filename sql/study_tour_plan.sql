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

 Date: 03/12/2025 17:41:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for study_tour_plan
-- ----------------------------
DROP TABLE IF EXISTS `study_tour_plan`;
CREATE TABLE `study_tour_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `plan_name` varchar(200) NOT NULL COMMENT '方案名称',
  `base_id` bigint NOT NULL COMMENT '所属基地ID（关联研学基地表）',
  `route` text NOT NULL COMMENT '研学路线（如：展厅参观→实践区→分享总结）',
  `brief_intro` text COMMENT '方案简介',
  `details` longtext COMMENT '方案具体内容',
  `suitable_crowd` varchar(200) DEFAULT NULL COMMENT '适用人群（如：小学生,初中生,亲子家庭）',
  `duration` varchar(50) NOT NULL COMMENT '研学时长（如：3小时/1天/2天1夜）',
  `status` tinyint DEFAULT '1' COMMENT '方案状态（1：启用；0：禁用）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_base_id` (`base_id`) COMMENT '基地ID索引，提升关联查询效率',
  KEY `idx_status` (`status`) COMMENT '方案状态索引',
  CONSTRAINT `fk_plan_base` FOREIGN KEY (`base_id`) REFERENCES `study_tour_base` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='研学方案表';

SET FOREIGN_KEY_CHECKS = 1;
