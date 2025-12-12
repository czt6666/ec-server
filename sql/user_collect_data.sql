-- 用户收藏测试数据
-- 注意：确保引用的 target_id 在对应的业务表中存在

-- 先删除测试数据（可选，如果不想删除已有数据，可以注释掉下面这行）
DELETE FROM user_collect WHERE user_id >= 1001 AND user_id <= 1010;

-- 使用 INSERT IGNORE 避免重复键错误（如果数据已存在则忽略）
INSERT IGNORE INTO user_collect (user_id, target_type, target_id, create_time) VALUES
-- 旅游线路收藏（假设 tour_route 表有 ID 1-6 的数据）
(1001, 'tour_route', '1', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1002, 'tour_route', '1', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1003, 'tour_route', '1', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1004, 'tour_route', '2', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1005, 'tour_route', '2', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1006, 'tour_route', '3', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1007, 'tour_route', '3', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1008, 'tour_route', '4', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1009, 'tour_route', '5', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1010, 'tour_route', '6', DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- 养老驿站收藏（假设 elderly_service_subject 表有 ID 1-5 的数据）
(1001, 'elderly_station', '1', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1002, 'elderly_station', '1', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1003, 'elderly_station', '2', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1004, 'elderly_station', '2', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1005, 'elderly_station', '3', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1006, 'elderly_station', '4', DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 研学方案收藏（假设 study_tour_plan 表有 ID 1-4 的数据）
(1001, 'study_plan', '1', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1002, 'study_plan', '1', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1003, 'study_plan', '1', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1004, 'study_plan', '2', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1005, 'study_plan', '2', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1006, 'study_plan', '3', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1007, 'study_plan', '4', DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 研学活动收藏（假设 study_tour_activity 表有 ID 1-6 的数据）
(1001, 'study_activity', '1', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1002, 'study_activity', '1', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1003, 'study_activity', '1', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1004, 'study_activity', '2', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1005, 'study_activity', '3', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1006, 'study_activity', '3', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1007, 'study_activity', '4', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1008, 'study_activity', '4', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1009, 'study_activity', '5', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1010, 'study_activity', '6', DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 民宿收藏（假设 village_homestay 表有 ID 1-6 的数据）
(1001, 'homestay', '1', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1002, 'homestay', '1', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1003, 'homestay', '2', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1004, 'homestay', '3', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1005, 'homestay', '4', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1006, 'homestay', '5', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1007, 'homestay', '5', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1008, 'homestay', '6', DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- 农产品收藏（假设 spu 表有 ID 1-8 的数据）
(1001, 'product', '1', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1002, 'product', '1', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1003, 'product', '1', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1004, 'product', '2', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1005, 'product', '3', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1006, 'product', '4', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1007, 'product', '5', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1008, 'product', '6', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1009, 'product', '7', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1010, 'product', '8', DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- 餐饮收藏（假设 restaurant 表有 ID 1-5 的数据）
(1001, 'restaurant', '1', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1002, 'restaurant', '1', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1003, 'restaurant', '2', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1004, 'restaurant', '3', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1005, 'restaurant', '4', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1006, 'restaurant', '5', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 说明：
-- 1. 数据分布在最近5天内，方便测试"近N天"的筛选功能
-- 2. 某些对象被多次收藏，用于测试热点统计功能
-- 3. 如果业务表中没有对应的ID，请先插入业务数据，或者修改这里的ID为实际存在的ID
