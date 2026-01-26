-- 强制删除指定权限：文章权限、证照权限、目录权限
-- 直接执行删除操作，不进行确认

-- ==================== 删除角色权限关联关系（先删除关联，避免外键约束）====================
DELETE FROM sys_role_permission 
WHERE permission_id IN (
    SELECT id FROM sys_permission 
    WHERE menu_name LIKE '%文章%' 
       OR menu_name LIKE '%证照%'
       OR menu_name LIKE '%目录%'
       OR permission_name LIKE '%文章%'
       OR permission_name LIKE '%证照%'
       OR permission_name LIKE '%目录%'
);

-- ==================== 删除权限 ====================
DELETE FROM sys_permission 
WHERE menu_name LIKE '%文章%' 
   OR menu_name LIKE '%证照%'
   OR menu_name LIKE '%目录%'
   OR permission_name LIKE '%文章%'
   OR permission_name LIKE '%证照%'
   OR permission_name LIKE '%目录%';

