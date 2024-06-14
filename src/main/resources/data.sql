create table bin
(
    created     timestamp(6) with time zone,
    delete_time timestamp(6) with time zone,
    id          bigint not null
        primary key,
    text        varchar(255),
    url         varchar(255)
);

create sequence bin_sequence;