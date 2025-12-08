CREATE TABLE IF NOT EXISTS route_type_dict (
                                               id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                                               type_name VARCHAR(100) NOT NULL COMMENT '类型名称，如 红色旅游/绿色旅游/组合',
    type_code VARCHAR(100) DEFAULT NULL COMMENT '类型编码，选填，便于前后端匹配',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序，越小越靠前',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_route_type_name (type_name),
    UNIQUE KEY uk_route_type_code (type_code),
    KEY idx_route_type_status (status)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅游路线类型';
