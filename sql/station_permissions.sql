-- 养老驿站管理权限配置 (menu_code = 'station')
-- 权限ID范围：2001-2006
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
(2001, 'station', '养老驿站管理', 'station:list',      '驿站列表', 1),
(2002, 'station', '养老驿站管理', 'station:add',       '新增驿站', 2),
(2003, 'station', '养老驿站管理', 'station:update',    '修改驿站', 2),
(2004, 'station', '养老驿站管理', 'station:delete',    '删除驿站', 2),
(2005, 'station', '养老驿站管理', 'station:publish',    '上架驿站', 2),
(2006, 'station', '养老驿站管理', 'station:unpublish', '下架驿站', 2);

