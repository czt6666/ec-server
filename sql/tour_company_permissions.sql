-- 旅游公司管理权限配置 (menu_code = 'tourCompany')
-- 权限ID范围：1701-1706
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
(1801, 'tourCompany', '旅游公司管理', 'tourCompany:list',      '旅游公司列表', 1),
(1802, 'tourCompany', '旅游公司管理', 'tourCompany:add',       '新增旅游公司', 2),
(1803, 'tourCompany', '旅游公司管理', 'tourCompany:update',    '修改旅游公司', 2),
(1804, 'tourCompany', '旅游公司管理', 'tourCompany:delete',    '删除旅游公司', 2),
(1805, 'tourCompany', '旅游公司管理', 'tourCompany:publish',    '上架旅游公司', 2),
(1806, 'tourCompany', '旅游公司管理', 'tourCompany:unpublish', '下架旅游公司', 2);

