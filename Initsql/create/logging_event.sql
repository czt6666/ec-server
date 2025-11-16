create table logging_event
(
	timestmp bigint not null,
	formatted_message text not null,
	logger_name varchar(254) not null,
	level_string varchar(254) not null,
	thread_name varchar(254) null,
	reference_flag smallint null,
	arg0 varchar(254) null,
	arg1 varchar(254) null,
	arg2 varchar(254) null,
	arg3 varchar(254) null,
	caller_filename varchar(254) not null,
	caller_class varchar(254) not null,
	caller_method varchar(254) not null,
	caller_line char(4) not null,
	event_id bigint auto_increment
		primary key
)
charset=utf8;

