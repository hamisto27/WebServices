# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table friend (
  friend_one                integer,
  friend_two                integer,
  status                    varchar(1) not null,
  since                     datetime not null,
  constraint ck_friend_status check (status in ('1','0','2')),
  constraint pk_friend primary key (friend_one, friend_two))
;

create table user (
  id                        integer(11) auto_increment not null,
  full_name                 varchar(255) not null,
  email_address             varchar(255) not null,
  sha_password              varbinary(64) not null,
  friend_count              integer(11) not null,
  creation_date             datetime not null,
  sha_auth_token            varbinary(64),
  constraint uq_user_email_address unique (email_address),
  constraint pk_user primary key (id))
;

alter table friend add constraint fk_friend_userFriendOne_1 foreign key (friend_one) references user (id) on delete restrict on update restrict;
create index ix_friend_userFriendOne_1 on friend (friend_one);
alter table friend add constraint fk_friend_userFriendTwo_2 foreign key (friend_two) references user (id) on delete restrict on update restrict;
create index ix_friend_userFriendTwo_2 on friend (friend_two);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table friend;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

