-- ============================================
-- 小程序用户表创建 SQL（完整版）
-- ============================================

-- 创建小程序用户表
CREATE TABLE IF NOT EXISTS mini_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    phone VARCHAR(11) NULL UNIQUE COMMENT '手机号（唯一，可为空）',
    username VARCHAR(50) NULL UNIQUE COMMENT '用户名（唯一，可为空）',
    password VARCHAR(255) NULL COMMENT '密码（加密存储）',
    nickname VARCHAR(50) NULL COMMENT '昵称',
    avatar VARCHAR(255) NULL COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0=未知，1=男，2=女',
    delete_status TINYINT DEFAULT 1 COMMENT '删除状态：0=禁用，1=启用，2=已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_time DATETIME NULL COMMENT '最后登录时间',
    INDEX idx_phone (phone),
    INDEX idx_username (username),
    INDEX idx_delete_status (delete_status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序用户表';

-- ============================================
-- 表结构说明：
-- 1. phone 字段设置为唯一索引，可为空（支持用户名登录）
-- 2. username 字段设置为唯一索引，可为空（支持手机号登录）
-- 3. password 字段存储加密后的密码（MD5 或 BCrypt）
-- 4. delete_status = 1 表示启用状态
-- 5. 使用 utf8mb4 字符集，支持 emoji 等特殊字符
-- 6. 自动维护 create_time 和 update_time
-- ============================================

-- ============================================
-- 测试数据（可选）
-- ============================================
-- 注意：密码需要加密后存储
-- MD5('123456') = e10adc3949ba59abbe56e057f20f883e
-- INSERT INTO mini_user (phone, username, password, nickname, delete_status, create_time, update_time, last_login_time)
-- VALUES ('13800138000', 'testuser', 'e10adc3949ba59abbe56e057f20f883e', '测试用户', 1, NOW(), NOW(), NOW());

-- ============================================
-- 索引说明：
-- 1. idx_phone: 手机号索引，用于快速查询
-- 2. idx_username: 用户名索引，用于快速查询
-- 3. idx_delete_status: 删除状态索引，用于查询启用用户
-- 4. idx_create_time: 创建时间索引，用于排序
-- ============================================



