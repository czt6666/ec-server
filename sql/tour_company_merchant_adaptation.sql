-- 旅游公司商户用户功能适配
-- 添加 user_id 列用于关联商家用户

ALTER TABLE tour_company
ADD COLUMN user_id BIGINT NULL COMMENT '关联用户ID（商家ID）' AFTER status;

-- 添加索引
CREATE INDEX idx_user_id ON tour_company(user_id);

