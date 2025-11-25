DROP TABLE IF EXISTS `restaurant`;
CREATE TABLE `restaurant` (
  `id`                 BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`            BIGINT       NOT NULL COMMENT '关联商家/管理员用户ID',
  `village_id`         INT          NOT NULL COMMENT '所属村ID',
  `name`               VARCHAR(100) NOT NULL COMMENT '门店名称（≤100字符）',
  `business_start_time` CHAR(5)     NOT NULL COMMENT '开始营业时间 HH:mm',
  `business_end_time`   CHAR(5)     NOT NULL COMMENT '结束营业时间 HH:mm',
  `logo_url`           VARCHAR(255) DEFAULT NULL COMMENT '门店Logo（单张图片URL）',
  `address`            VARCHAR(200) NOT NULL COMMENT '门店地址（≤200字符）',
  `coordinate_lat`     DECIMAL(10,6) DEFAULT NULL COMMENT '纬度',
  `coordinate_lng`     DECIMAL(10,6) DEFAULT NULL COMMENT '经度',
  `phone`              VARCHAR(20)   DEFAULT NULL COMMENT '联系电话（≤20字符）',
  `notice`             VARCHAR(500)  DEFAULT NULL COMMENT '门店公告（≤500字符）',
  `license_urls`       TEXT          DEFAULT NULL COMMENT '证照图片JSON，含营业执照/食品许可证，单类≤15张',
  `status`             TINYINT(1)    NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-停用',
  `create_time`        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time`        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_restaurant_user` (`user_id`),
  KEY `idx_restaurant_village` (`village_id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐饮商铺表';
