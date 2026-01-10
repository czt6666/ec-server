-- 民宿管理权限配置 (menu_code = 'villageHomestay')
-- 权限ID范围：1601-1606
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
(1701, 'villageHomestay', '民宿管理', 'villageHomestay:list',      '民宿列表', 1),
(1702, 'villageHomestay', '民宿管理', 'villageHomestay:add',       '新增民宿', 2),
(1703, 'villageHomestay', '民宿管理', 'villageHomestay:update',    '修改民宿', 2),
(1704, 'villageHomestay', '民宿管理', 'villageHomestay:delete',    '删除民宿', 2),
(1705, 'villageHomestay', '民宿管理', 'villageHomestay:publish',    '上架民宿', 2),
(1706, 'villageHomestay', '民宿管理', 'villageHomestay:unpublish', '下架民宿', 2);

