-- 为研学基地表添加商户模式支持
-- 添加 user_id 字段，关联后台用户（商户）
ALTER TABLE study_tour_base
ADD COLUMN user_id BIGINT COMMENT '关联用户ID（商家ID）' AFTER id;

-- 添加索引，提升查询效率
ALTER TABLE study_tour_base
ADD INDEX idx_user_id (user_id);

