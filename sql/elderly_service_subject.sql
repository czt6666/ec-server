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

 Date: 03/12/2025 16:36:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for elderly_service_subject
-- ----------------------------
DROP TABLE IF EXISTS `elderly_service_subject`;
CREATE TABLE `elderly_service_subject` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(200) NOT NULL COMMENT '主体名称（如XX养老服务中心）',
  `registered_address` varchar(500) NOT NULL COMMENT '注册地址',
  `business_address` varchar(500) NOT NULL COMMENT '经营地址',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '地址纬度',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '地址经度',
  `unified_social_credit_code` varchar(50) NOT NULL COMMENT '统一社会信用代码',
  `legal_representative` varchar(100) NOT NULL COMMENT '法定代表人',
  `registered_capital` decimal(15,2) DEFAULT NULL COMMENT '注册资本（万元）',
  `establishment_date` date DEFAULT NULL COMMENT '成立日期',
  `business_term` varchar(200) DEFAULT NULL COMMENT '营业期限（如：长期/2020-01-01至2050-01-01）',
  `official_phone` varchar(50) NOT NULL COMMENT '官方联系电话',
  `emergency_contact` varchar(100) NOT NULL COMMENT '紧急联系人',
  `emergency_phone` varchar(50) NOT NULL COMMENT '紧急联系电话',
  `official_email` varchar(100) DEFAULT NULL COMMENT '官方邮箱',
  `subject_type_id` bigint NOT NULL COMMENT '主体类型ID（关联主体类型表）',
  `elderly_license_no` varchar(100) DEFAULT NULL COMMENT '养老机构设立许可证编号',
  `medical_license_no` varchar(100) DEFAULT NULL COMMENT '医疗机构执业许可证编号（如有）',
  `food_license_no` varchar(100) DEFAULT NULL COMMENT '食品经营许可证编号',
  `fire_acceptance_no` varchar(100) DEFAULT NULL COMMENT '消防验收合格证明编号',
  `business_status` tinyint NOT NULL COMMENT '营业状态（1：营业中；2：暂停营业；3：已注销）',
  `total_beds` int DEFAULT '0' COMMENT '总床数',
  `room_config` varchar(100) DEFAULT NULL COMMENT '房型配置（多选，逗号分隔，如：单人,多人）',
  `care_level` varchar(200) DEFAULT NULL COMMENT '护理等级（多选，逗号分隔，如：自理,半自理,非自理）',
  `price_range` varchar(100) DEFAULT NULL COMMENT '价格区间（如：2000-5000元/月）',
  `environment_photos` text COMMENT '环境照片（多个URL用逗号分隔）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_credit_code` (`unified_social_credit_code`) COMMENT '统一社会信用代码唯一',
  KEY `idx_subject_type` (`subject_type_id`) COMMENT '主体类型索引，提升关联查询效率',
  CONSTRAINT `fk_subject_type` FOREIGN KEY (`subject_type_id`) REFERENCES `elderly_subject_type` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='养老服务主体信息表';

SET FOREIGN_KEY_CHECKS = 1;
