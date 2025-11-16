create table logging_event_exception
(
	event_id bigint not null,
	i smallint not null,
	trace_line varchar(254) not null,
	primary key (event_id, i),
	constraint logging_event_exception_ibfk_1
		foreign key (event_id) references logging_event (event_id)
)
charset=utf8;

