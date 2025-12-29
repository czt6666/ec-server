-- 为旅游线路表添加公司ID字段，建立与旅游公司表的关联
-- 执行时间：2024-01-XX

-- 1. 添加 company_id 字段
ALTER TABLE tour_route 
ADD COLUMN company_id BIGINT UNSIGNED DEFAULT NULL COMMENT '关联的公司ID（外键关联tour_company表）' AFTER status;

-- 2. 添加外键约束（可选，如果不需要外键约束可以跳过）
-- ALTER TABLE tour_route 
-- ADD CONSTRAINT fk_tour_route_company 
-- FOREIGN KEY (company_id) REFERENCES tour_company(id) 
-- ON DELETE SET NULL ON UPDATE CASCADE;

-- 3. 添加索引以提高查询性能
ALTER TABLE tour_route 
ADD INDEX idx_route_company_id (company_id);

-- 4. 如果需要为现有数据设置默认公司，可以执行以下SQL（请根据实际情况修改）
-- UPDATE tour_route SET company_id = 1 WHERE company_id IS NULL;

