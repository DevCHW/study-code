drop table if exists user;

create table user
(
    id         bigint auto_increment
        primary key,
    age        int           not null,
    name       varchar(255)  not null,
    email      varchar(255)  not null,
    created_at timestamp(6)  not null,
    updated_at timestamp(6)  not null
);

