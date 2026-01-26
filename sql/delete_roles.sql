-- 强制删除指定角色
-- 直接执行删除操作，不进行确认

-- ==================== 删除用户角色关联关系（先删除关联，避免外键约束）====================
DELETE FROM sys_user_role
WHERE role_id IN (
    SELECT id FROM sys_role
    WHERE role_name IN ('作家?', '程序员', '只有文章', 'test1', 'test11')
);

-- ==================== 删除角色 ====================
DELETE FROM sys_role
WHERE role_name IN ('作家?', '程序员', '只有文章', 'test1', 'test11');

-- 如果角色名称不完全匹配，可以使用模糊匹配（谨慎使用）
-- DELETE FROM user_role
-- WHERE role_id IN (
--     SELECT id FROM sys_role
--     WHERE role_name LIKE '%作家%'
--        OR role_name LIKE '%程序员%'
--        OR role_name LIKE '%只有文章%'
--        OR role_name LIKE '%test1%'
--        OR role_name LIKE '%test11%'
-- );
--
-- DELETE FROM sys_role
-- WHERE role_name LIKE '%作家%'
--    OR role_name LIKE '%程序员%'
--    OR role_name LIKE '%只有文章%'
--    OR role_name LIKE '%test1%'
--    OR role_name LIKE '%test11%';

