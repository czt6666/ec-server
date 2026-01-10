-- 民宿模块商家用户功能适配SQL脚本
-- 1. 添加user_id字段（商家ID）
ALTER TABLE village_homestay 
ADD COLUMN user_id BIGINT COMMENT '关联用户ID（商家ID）' AFTER id;

-- 2. 添加索引
ALTER TABLE village_homestay 
ADD INDEX idx_user_id (user_id);

-- 3. 更新status字段注释：0-待审核 1-营业（已上架） 2-暂停营业 3-已下架
ALTER TABLE village_homestay 
MODIFY COLUMN `status` TINYINT NOT NULL DEFAULT 0 COMMENT '营业状态：0-待审核 1-营业（已上架） 2-暂停营业 3-已下架';

-- 4. 将现有数据的status更新为1（已上架），user_id设置为NULL（历史数据）
UPDATE village_homestay 
SET status = 1, user_id = NULL 
WHERE user_id IS NULL;

