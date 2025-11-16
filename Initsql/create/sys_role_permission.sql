create table sys_role_permission
(
	id int auto_increment
		primary key,
	role_id int null comment '角色id',
	permission_id int null comment '权限id',
	create_time timestamp default CURRENT_TIMESTAMP null,
	update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	delete_status varchar(1) default '1' null comment '是否有效 1有效     2无效'
)
comment '角色-权限关联表' charset=utf8mb4;

