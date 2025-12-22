-- 菜品分类管理权限 (menu_code = 'dishCategory')
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
                                                                                                                 (1401, 'dishCategory', '菜品分类管理', 'dishCategory:list',   '菜品分类列表', 1),
                                                                                                                 (1402, 'dishCategory', '菜品分类管理', 'dishCategory:add',    '新增菜品分类', 2),
                                                                                                                 (1403, 'dishCategory', '菜品分类管理', 'dishCategory:update', '修改菜品分类', 2),
                                                                                                                 (1404, 'dishCategory', '菜品分类管理', 'dishCategory:delete', '删除菜品分类', 2);

-- 菜品管理权限 (menu_code = 'dish')
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
                                                                                                                 (1501, 'dish', '菜品管理', 'dish:list',   '菜品列表', 1),
                                                                                                                 (1502, 'dish', '菜品管理', 'dish:add',    '新增菜品', 2),
                                                                                                                 (1503, 'dish', '菜品管理', 'dish:update', '修改菜品', 2),
                                                                                                                 (1504, 'dish', '菜品管理', 'dish:delete', '删除菜品', 2)
