CREATE TABLE village_homestay (
                          id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
                          village_id INT NOT NULL COMMENT '所属乡村ID，逻辑外键→village.id',
                          homestay_name VARCHAR(100) NOT NULL COMMENT '民宿名称',
                          address VARCHAR(200) COMMENT '民宿详细地址（可冗余，也可直接挂到乡村）',
                          `status` TINYINT NOT NULL DEFAULT 1 COMMENT '营业状态：1-营业 2-暂停营业 3-已下架',
                          star_level TINYINT DEFAULT 0 COMMENT '星级/等级：0-未评 1-5星',
                          room_count INT NOT NULL DEFAULT 0 COMMENT '客房数量',
                          bed_count INT NOT NULL DEFAULT 0 COMMENT '床位总数',
                          max_capacity INT NOT NULL DEFAULT 0 COMMENT '最大接待人数',
                          contact_name VARCHAR(50) COMMENT '负责人姓名',
                          contact_phone VARCHAR(20) COMMENT '负责人电话',
                          description VARCHAR(200) COMMENT '民宿简介、特色亮点',
                          price DECIMAL(8,2) COMMENT '房价:元/月',
                          cover_image VARCHAR(255) COMMENT '封面图URL',
                          create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                          CONSTRAINT fk_homestay_village FOREIGN KEY (village_id) REFERENCES village(id),

                          INDEX idx_village_id (village_id),
                          INDEX idx_status_star (status, star_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='乡村民宿信息表';