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

 Date: 24/12/2025 23:39:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for study_tour_activity
-- ----------------------------
DROP TABLE IF EXISTS `study_tour_activity`;
CREATE TABLE `study_tour_activity` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `activity_name` varchar(200) NOT NULL COMMENT '活动名称',
  `plan_id` bigint NOT NULL COMMENT '关联研学方案ID',
  `apply_start_date` varchar(20) NOT NULL COMMENT '报名开始日期',
  `apply_end_date` varchar(20) NOT NULL COMMENT '报名结束日期',
  `activity_start_date` varchar(20) NOT NULL COMMENT '活动开始日期',
  `activity_end_date` varchar(20) NOT NULL COMMENT '活动结束日期',
  `price` decimal(10,2) NOT NULL COMMENT '活动价格（元/人）',
  `recruit_num` int NOT NULL COMMENT '招生总人数',
  `registered_num` int DEFAULT '0' COMMENT '已报名人数',
  `status` tinyint DEFAULT '1' COMMENT '活动状态（1：报名中；2：报名结束；3：活动进行中；4：活动结束；5：取消）',
  `remark` text COMMENT '活动备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_plan_id` (`plan_id`) COMMENT '方案ID索引，提升关联查询效率',
  KEY `idx_apply_date` (`apply_start_date`,`apply_end_date`) COMMENT '报名日期索引，便于筛选报名中活动',
  CONSTRAINT `fk_activity_plan` FOREIGN KEY (`plan_id`) REFERENCES `study_tour_plan` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='研学活动表';

SET FOREIGN_KEY_CHECKS = 1;
