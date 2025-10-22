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

 Date: 22/10/2025 20:08:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for village_news
-- ----------------------------
DROP TABLE IF EXISTS `village_news`;
CREATE TABLE `village_news` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL COMMENT '新闻标题',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '编写用户',
  `village_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '隶属乡村ID',
  `theme_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '主题名称',
  `image_url` varchar(1024) DEFAULT NULL COMMENT '封面图片URL（单张）',
  `content` mediumtext NOT NULL COMMENT '新闻内容',
  `published_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除(0否,1是)',
  `publish_status` tinyint NOT NULL DEFAULT '1' COMMENT '发布状态：1-草稿 2-已发布 3-已下线',
  PRIMARY KEY (`id`),
  KEY `idx_author_id` (`author`),
  KEY `idx_village_id` (`village_name`),
  KEY `idx_theme_id` (`theme_name`),
  KEY `idx_published_at` (`published_time`),
  CONSTRAINT `village` FOREIGN KEY (`village_name`) REFERENCES `village` (`village_name`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='新闻表';

SET FOREIGN_KEY_CHECKS = 1;
