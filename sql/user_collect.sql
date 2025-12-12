CREATE TABLE IF NOT EXISTS user_collect (
  id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  user_id INT NOT NULL COMMENT '村民ID',
  target_type VARCHAR(20) NOT NULL COMMENT '收藏对象：方案/活动（plan/activity）',
  target_id VARCHAR(50) NOT NULL COMMENT '方案ID/活动ID',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY uk_user_target (user_id, target_type, target_id) COMMENT '防止重复收藏',
  KEY idx_target (target_type, target_id),
  KEY idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表（兼顾热点统计）';


