-- 添加营业时间字段到研学基地表
ALTER TABLE `study_tour_base` 
ADD COLUMN `business_hours_start` time DEFAULT NULL COMMENT '营业开始时间',
ADD COLUMN `business_hours_end` time DEFAULT NULL COMMENT '营业结束时间';