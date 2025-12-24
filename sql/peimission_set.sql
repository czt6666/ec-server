-- 操作日志权限 (menu_code = 'operateLog')
INSERT INTO sys_permission (id, menu_code, menu_name, permission_code, permission_name, required_permission) VALUES
    (1601, 'operateLog', '操作日志', 'operateLog:list', '查看操作日志', 1);
