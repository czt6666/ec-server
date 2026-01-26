-- 删除指定权限：文章权限、证照权限、目录权限
-- 删除前先检查这些权限是否存在，以及是否有角色关联

-- 查看要删除的权限信息（根据权限名称模糊匹配）
SELECT id, menu_code, menu_name, permission_code, permission_name, required_permission
FROM sys_permission 
WHERE menu_name LIKE '%文章%' 
   OR menu_name LIKE '%证照%'
   OR menu_name LIKE '%目录%'
   OR permission_name LIKE '%文章%'
   OR permission_name LIKE '%证照%'
   OR permission_name LIKE '%目录%'
   OR menu_code LIKE '%article%'
   OR menu_code LIKE '%license%'
   OR menu_code LIKE '%qualification%'
   OR menu_code LIKE '%directory%'
   OR menu_code LIKE '%menu%'
   OR menu_code LIKE '%category%'
   OR permission_code LIKE '%article%'
   OR permission_code LIKE '%license%'
   OR permission_code LIKE '%qualification%'
   OR permission_code LIKE '%directory%'
   OR permission_code LIKE '%menu%'
   OR permission_code LIKE '%category%';

-- 检查这些权限是否有角色关联
SELECT rp.role_id, r.role_name, p.id as permission_id, p.menu_name, p.permission_name
FROM role_permission rp
INNER JOIN sys_permission p ON rp.permission_id = p.id
INNER JOIN sys_role r ON rp.role_id = r.id
WHERE p.menu_name LIKE '%文章%' 
   OR p.menu_name LIKE '%证照%'
   OR p.menu_name LIKE '%目录%'
   OR p.permission_name LIKE '%文章%'
   OR p.permission_name LIKE '%证照%'
   OR p.permission_name LIKE '%目录%'
   OR p.menu_code LIKE '%article%'
   OR p.menu_code LIKE '%license%'
   OR p.menu_code LIKE '%qualification%'
   OR p.menu_code LIKE '%directory%'
   OR p.menu_code LIKE '%menu%'
   OR p.menu_code LIKE '%category%'
   OR p.permission_code LIKE '%article%'
   OR p.permission_code LIKE '%license%'
   OR p.permission_code LIKE '%qualification%'
   OR p.permission_code LIKE '%directory%'
   OR p.permission_code LIKE '%menu%'
   OR p.permission_code LIKE '%category%';

-- 删除角色权限关联关系（先删除关联，避免外键约束）
DELETE FROM role_permission 
WHERE permission_id IN (
    SELECT id FROM sys_permission 
    WHERE menu_name LIKE '%文章%' 
       OR menu_name LIKE '%证照%'
       OR menu_name LIKE '%目录%'
       OR permission_name LIKE '%文章%'
       OR permission_name LIKE '%证照%'
       OR permission_name LIKE '%目录%'
       OR menu_code LIKE '%article%'
       OR menu_code LIKE '%license%'
       OR menu_code LIKE '%qualification%'
       OR menu_code LIKE '%directory%'
       OR menu_code LIKE '%menu%'
       OR menu_code LIKE '%category%'
       OR permission_code LIKE '%article%'
       OR permission_code LIKE '%license%'
       OR permission_code LIKE '%qualification%'
       OR permission_code LIKE '%directory%'
       OR permission_code LIKE '%menu%'
       OR permission_code LIKE '%category%'
);

-- 删除权限
DELETE FROM sys_permission 
WHERE menu_name LIKE '%文章%' 
   OR menu_name LIKE '%证照%'
   OR menu_name LIKE '%目录%'
   OR permission_name LIKE '%文章%'
   OR permission_name LIKE '%证照%'
   OR permission_name LIKE '%目录%'
   OR menu_code LIKE '%article%'
   OR menu_code LIKE '%license%'
   OR menu_code LIKE '%qualification%'
   OR menu_code LIKE '%directory%'
   OR menu_code LIKE '%menu%'
   OR menu_code LIKE '%category%'
   OR permission_code LIKE '%article%'
   OR permission_code LIKE '%license%'
   OR permission_code LIKE '%qualification%'
   OR permission_code LIKE '%directory%'
   OR permission_code LIKE '%menu%'
   OR permission_code LIKE '%category%';

-- 如果权限名称完全匹配，可以使用精确匹配（更安全）
-- 请先执行查询语句确认权限ID，然后使用以下精确删除语句：
-- DELETE FROM role_permission WHERE permission_id IN (权限ID列表);
-- DELETE FROM sys_permission WHERE id IN (权限ID列表);

