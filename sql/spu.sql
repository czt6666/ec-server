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

 Date: 20/01/2026 00:09:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for spu
-- ----------------------------
DROP TABLE IF EXISTS `spu`;
CREATE TABLE `spu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(120) NOT NULL COMMENT '商品标题',
  `user_id` bigint NOT NULL COMMENT '后台创建者(用户)ID',
  `intro` varchar(500) DEFAULT NULL COMMENT '商品简介（列表页短描述）',
  `link` varchar(255) DEFAULT NULL COMMENT '外部跳转链接（可为空）',
  `status` tinyint DEFAULT '1' COMMENT '0下架 1上架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `view_cnt` int unsigned NOT NULL DEFAULT '0' COMMENT '浏览量',
  `cart_add_cnt` int unsigned NOT NULL DEFAULT '0' COMMENT '被加入购物车次数',
  `mini_program_appid` varchar(100) DEFAULT NULL COMMENT '小程序APPID',
  `mini_program_path` varchar(255) DEFAULT NULL COMMENT '小程序页面路径',
  `micro_shop_appid` varchar(100) DEFAULT NULL COMMENT '微店APPID',
  `micro_shop_product_id` varchar(100) DEFAULT NULL COMMENT '微店商品ID',
  `merchant_poster_img` varchar(500) DEFAULT NULL COMMENT '商家海报图片',
  `upload_method_status` tinyint DEFAULT '0' COMMENT '上传方式状态：0-无，1-仅微店，2-仅小程序，3-微店与小程序都有',
  PRIMARY KEY (`id`),
  KEY `idx_shop_status` (`user_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
