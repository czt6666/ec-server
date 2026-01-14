-- 旅游路线管理权限配置 (menu_code = 'tourRoute')
-- 权限ID范围：1901-1904
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
(1901, 'tourRoute', '旅游路线管理', 'tourRoute:list',      '旅游路线列表', 1),
(1902, 'tourRoute', '旅游路线管理', 'tourRoute:add',       '新增旅游路线', 2),
(1903, 'tourRoute', '旅游路线管理', 'tourRoute:update',    '修改旅游路线', 2),
(1904, 'tourRoute', '旅游路线管理', 'tourRoute:delete',    '删除旅游路线', 2);

