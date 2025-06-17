-- 아래 테이블 생성 DDL 문은 예시입니다.
drop table if exists example_users cascade;

create table example_users
(
  id bigint not null comment 'PK'
    primary key auto_increment,
  username   varchar(255) not null comment '이름',
  created_at timestamp(6) not null comment '생성 시점',
  updated_at timestamp(6) not null comment '마지막 수정 시점'
)
  comment '유저';
