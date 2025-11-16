create table logging_event_property
(
	event_id bigint not null,
	mapped_key varchar(254) not null,
	mapped_value text null,
	primary key (event_id, mapped_key),
	constraint logging_event_property_ibfk_1
		foreign key (event_id) references logging_event (event_id)
)
charset=utf8;

