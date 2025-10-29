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

 Date: 29/10/2025 20:14:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for news_theme
-- ----------------------------
DROP TABLE IF EXISTS `news_theme`;
CREATE TABLE `news_theme` (
  `news_id` int NOT NULL,
  `theme_id` int NOT NULL,
  PRIMARY KEY (`news_id`,`theme_id`),
  KEY `idx_theme` (`theme_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
