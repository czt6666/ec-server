create table sys_permission
(
	id int default 0 not null comment '自定id,主要供前端展示权限列表分类排序使用.'
		primary key,
	menu_code varchar(255) default '' null comment '归属菜单,前端判断并展示菜单使用,',
	menu_name varchar(255) default '' null comment '菜单的中文释义',
	permission_code varchar(255) default '' null comment '权限的代码/通配符,对应代码中@RequiresPermissions 的value',
	permission_name varchar(255) default '' null comment '本权限的中文释义',
	required_permission tinyint(1) default 2 null comment '是否本菜单必选权限, 1.必选 2非必选 通常是"列表"权限是必选'
)
comment '后台权限表' charset=utf8mb4;

