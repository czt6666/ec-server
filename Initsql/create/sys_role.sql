create table sys_role
(
	id int auto_increment
		primary key,
	role_name varchar(20) null comment '角色名',
	create_time timestamp default CURRENT_TIMESTAMP null,
	update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	delete_status varchar(1) default '1' null comment '是否有效  1有效  2无效'
)
comment '后台角色表' charset=utf8mb4;

