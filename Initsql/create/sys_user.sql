create table sys_user
(
	id int auto_increment
		primary key,
	username varchar(255) null comment '用户名',
	password varchar(1000) collate utf8mb4_unicode_ci null,
	nickname varchar(255) null comment '昵称',
	create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
	update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
	delete_status varchar(1) default '1' null comment '是否有效  1有效  2无效'
)
comment '用户表' charset=utf8mb4;

