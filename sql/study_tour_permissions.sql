-- 研学模块权限配置
-- 权限ID范围：2101-2404

-- ==================== 1. 研学基地管理权限 (menu_code = 'studyBase') ====================
-- 权限ID范围：2101-2106
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
(2101, 'studyBase', '研学基地管理', 'studyBase:list',      '研学基地列表', 1),
(2102, 'studyBase', '研学基地管理', 'studyBase:add',       '新增研学基地', 2),
(2103, 'studyBase', '研学基地管理', 'studyBase:update',    '修改研学基地', 2),
(2104, 'studyBase', '研学基地管理', 'studyBase:delete',    '删除研学基地', 2),
(2105, 'studyBase', '研学基地管理', 'studyBase:publish',    '上架研学基地', 2),
(2106, 'studyBase', '研学基地管理', 'studyBase:unpublish', '下架研学基地', 2);

-- ==================== 2. 研学方案管理权限 (menu_code = 'studyPlan') ====================
-- 权限ID范围：2201-2206
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
(2201, 'studyPlan', '研学方案管理', 'studyPlan:list',      '研学方案列表', 1),
(2202, 'studyPlan', '研学方案管理', 'studyPlan:add',       '新增研学方案', 2),
(2203, 'studyPlan', '研学方案管理', 'studyPlan:update',    '修改研学方案', 2),
(2204, 'studyPlan', '研学方案管理', 'studyPlan:delete',    '删除研学方案', 2),
(2205, 'studyPlan', '研学方案管理', 'studyPlan:publish',   '上架研学方案', 2),
(2206, 'studyPlan', '研学方案管理', 'studyPlan:unpublish', '下架研学方案', 2);

-- ==================== 3. 研学活动管理权限 (menu_code = 'studyActivity') ====================
-- 权限ID范围：2301-2304
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
(2301, 'studyActivity', '研学活动管理', 'studyActivity:list',      '研学活动列表', 1),
(2302, 'studyActivity', '研学活动管理', 'studyActivity:add',       '新增研学活动', 2),
(2303, 'studyActivity', '研学活动管理', 'studyActivity:update',    '修改研学活动', 2),
(2304, 'studyActivity', '研学活动管理', 'studyActivity:delete',    '删除研学活动', 2);

-- ==================== 4. 研学类型管理权限 (menu_code = 'studyType') ====================
-- 权限ID范围：2401-2404
-- 注意：研学类型是系统级字典配置（红色教育/户外拓展/亲子研学等），应该只给管理员权限
-- 普通商户只需要在选择基地类型时使用这些类型，不需要创建/修改/删除
-- 如果需要给管理员配置权限，可以执行以下SQL；如果不需要权限控制，可以删除这部分
-- INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
-- (2401, 'studyType', '研学类型管理', 'studyType:list',      '研学类型列表', 1),
-- (2402, 'studyType', '研学类型管理', 'studyType:add',       '新增研学类型', 2),
-- (2403, 'studyType', '研学类型管理', 'studyType:update',    '修改研学类型', 2),
-- (2404, 'studyType', '研学类型管理', 'studyType:delete',    '删除研学类型', 2);

