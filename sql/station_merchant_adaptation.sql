-- 养老驿站模块商家用户功能适配SQL脚本
-- 1. 添加user_id字段（商家ID）
ALTER TABLE elderly_service_subject 
ADD COLUMN user_id BIGINT NULL COMMENT '关联用户ID（商家ID）' AFTER id;

-- 2. 添加索引
ALTER TABLE elderly_service_subject 
ADD INDEX idx_user_id (user_id);

-- 3. 将现有数据的user_id设置为NULL（历史数据，表示由管理员创建）
UPDATE elderly_service_subject 
SET user_id = NULL 
WHERE user_id IS NULL;

