-- 民宿模块小程序配置字段添加SQL脚本
-- 1. 添加小程序配置字段
ALTER TABLE village_homestay
ADD COLUMN mini_program_appid VARCHAR(100) DEFAULT NULL COMMENT '小程序APPID' AFTER cover_image,
ADD COLUMN mini_program_path VARCHAR(255) DEFAULT NULL COMMENT '小程序页面路径' AFTER mini_program_appid;

-- 2. 修改 cover_image 字段注释，说明支持多张图片（JSON数组格式）
ALTER TABLE village_homestay
MODIFY COLUMN cover_image TEXT COMMENT '封面图（JSON数组格式，支持多张图片）';
