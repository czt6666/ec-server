CREATE TABLE village (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    village_name VARCHAR(100) NOT NULL COMMENT '乡村名称',
    address VARCHAR(200) COMMENT '详细地址',
    village_description VARCHAR(200) COMMENT '乡村描述',
    manager_count INT DEFAULT 150 COMMENT '乡村管理人数',
    household_count INT COMMENT '户数',
    secretary_name VARCHAR(50) COMMENT '村书记姓名',
    secretary_phone VARCHAR(20) COMMENT '村书记联系方式',
    total_area DECIMAL(10,2) COMMENT '总面积（亩）',
    farmland_area DECIMAL(10,2) COMMENT '耕地面积（亩）',
    forest_area DECIMAL(10,2) COMMENT '林地面积（亩）',
    water_area DECIMAL(10,2) COMMENT '水域面积（亩）',
    construction_area DECIMAL(10,2) COMMENT '建设用地面积（亩）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) 