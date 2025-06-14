drop table if exists users cascade;
drop table if exists point cascade;

create table users
(
  id         bigint       not null comment 'PK'
    primary key auto_increment,
  username   varchar(255) not null comment '이름',
  created_at timestamp(6) not null comment '생성 시점',
  updated_at timestamp(6) not null comment '마지막 수정 시점'
)
  comment '유저';

create table point
(
  id      BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  balance INT    NOT NULL,
  version BIGINT NOT NULL
)
  comment '포인트';
