CREATE TABLE IF NOT EXISTS route_theme_dict (
                                                id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                                theme_name VARCHAR(100) NOT NULL COMMENT '主题标签名称，如 西红东绿/红色教育/生态康养',
    theme_code VARCHAR(100) DEFAULT NULL COMMENT '主题编码，选填',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序，越小越靠前',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_route_theme_name (theme_name),
    UNIQUE KEY uk_route_theme_code (theme_code),
    KEY idx_route_theme_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅游路线主题';
