CREATE TABLE IF NOT EXISTS operate_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  user_id BIGINT NOT NULL COMMENT '操作用户ID',
  username VARCHAR(50) NOT NULL COMMENT '操作用户名',
  operation VARCHAR(255) NOT NULL COMMENT '操作类型（如：新增、修改、删除等）',
  method VARCHAR(255) NOT NULL COMMENT '请求方法（完整类名.方法名）',
  request_params TEXT COMMENT '请求参数',
  response_result TEXT COMMENT '响应结果',
  ip_address VARCHAR(64) COMMENT 'IP地址',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  cost_time BIGINT NOT NULL COMMENT '耗时（毫秒）',
  success TINYINT(1) DEFAULT 1 COMMENT '是否成功（0-失败，1-成功）',
  error_message TEXT COMMENT '错误信息',
  INDEX idx_user_id (user_id),
  INDEX idx_username (username),
  INDEX idx_operation (operation),
  INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';