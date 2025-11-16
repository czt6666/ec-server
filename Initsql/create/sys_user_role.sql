create table sys_user_role
(
	id int auto_increment
		primary key,
	user_id int not null comment '用户id',
	role_id int null comment '角色id'
)
comment '用户-角色关联表' charset=utf8mb4;

