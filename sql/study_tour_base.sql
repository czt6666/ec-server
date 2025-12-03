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

 Date: 03/12/2025 17:41:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for study_tour_base
-- ----------------------------
DROP TABLE IF EXISTS `study_tour_base`;
CREATE TABLE `study_tour_base` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `base_name` varchar(200) NOT NULL COMMENT '研学基地名称',
  `operation_unit` varchar(200) NOT NULL COMMENT '运行单位',
  `address` varchar(500) NOT NULL COMMENT '基地详细地址',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '地址纬度',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '地址经度',
  `legal_representative` varchar(100) DEFAULT NULL COMMENT '法定代表人',
  `unified_social_credit_code` varchar(50) DEFAULT NULL COMMENT '统一社会信用代码',
  `qualification_cert` text COMMENT '资质证明（多个URL用逗号分隔）',
  `feature_desc` text COMMENT '基地特色说明',
  `business_status` tinyint NOT NULL COMMENT '营业状态（1：营业中；2：暂停营业；3：已注销）',
  `contact_person` varchar(100) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_base_name` (`base_name`) COMMENT '基地名称唯一',
  KEY `idx_business_status` (`business_status`) COMMENT '营业状态索引，提升筛选效率'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='研学基地主体信息表';

SET FOREIGN_KEY_CHECKS = 1;
