INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission)
VALUES
    (1101, 'product', '农产品管理', 'product:list',   '商品列表', 1),
    (1103, 'product', '农产品管理', 'product:update', '商品编辑', 2),
    (1102, 'product', '农产品管理', 'product:add',    '新增商品', 2),
    (1104, 'product', '农产品管理', 'product:delete', '删除商品', 2);


INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES

                                                                                                                 (1202, 'restaurant', '餐饮门店', 'restaurant:add',    '新增门店', 2),
                                                                                                                 (1203, 'restaurant', '餐饮门店', 'restaurant:update', '修改门店', 2),
                                                                                                                 (1204, 'restaurant', '餐饮门店', 'restaurant:delete', '删除门店', 2),
                                                                                                                 (1201, 'restaurant', '餐饮门店', 'restaurant:list', '门店列表', 1);



-- 店铺管理权限 (menu_code = 'shop')  1801-1805
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
                                                                                                                 (1301, 'shop', '农产品店铺', 'shop:list',      '店铺列表', 1),
                                                                                                                 (1302, 'shop', '农产品店铺', 'shop:add',       '新增店铺', 2),
                                                                                                                 (1303, 'shop', '农产品店铺', 'shop:update',    '修改店铺', 2),
                                                                                                                 (1304, 'shop', '农产品店铺', 'shop:delete',    '删除店铺', 2);
