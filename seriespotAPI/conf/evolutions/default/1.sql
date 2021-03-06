# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                        integer auto_increment not null,
  message                   TEXT,
  series_id                 varchar(11),
  user_id                   integer(11),
  constraint pk_comment primary key (id))
;

create table friend (
  friend_one                integer,
  friend_two                integer,
  status                    varchar(1) not null,
  since                     datetime not null,
  constraint ck_friend_status check (status in ('0','1','2')),
  constraint pk_friend primary key (friend_one, friend_two))
;

create table rating (
  id                        integer(11) auto_increment not null,
  series_id                 varchar(11),
  total                     integer,
  votes                     integer,
  constraint pk_rating primary key (id))
;

create table series (
  id                        varchar(11) not null,
  name                      varchar(255),
  overview                  TEXT,
  genre                     varchar(255),
  poster                    varchar(150),
  status                    varchar(1),
  rating_tvdb               varchar(11),
  rating                    float(11),
  constraint ck_series_status check (status in ('1','0')),
  constraint pk_series primary key (id))
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

create table user_series (
  user_id                   integer(11),
  series_id                 varchar(11),
  creation_date             datetime,
  rate                      integer,
  constraint pk_user_series primary key (user_id, series_id))
;

alter table comment add constraint fk_comment_series_1 foreign key (series_id) references series (id) on delete restrict on update restrict;
create index ix_comment_series_1 on comment (series_id);
alter table comment add constraint fk_comment_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_comment_user_2 on comment (user_id);
alter table friend add constraint fk_friend_userFriendOne_3 foreign key (friend_one) references user (id) on delete restrict on update restrict;
create index ix_friend_userFriendOne_3 on friend (friend_one);
alter table friend add constraint fk_friend_userFriendTwo_4 foreign key (friend_two) references user (id) on delete restrict on update restrict;
create index ix_friend_userFriendTwo_4 on friend (friend_two);
alter table rating add constraint fk_rating_series_5 foreign key (series_id) references series (id) on delete restrict on update restrict;
create index ix_rating_series_5 on rating (series_id);
alter table user_series add constraint fk_user_series_user_6 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_series_user_6 on user_series (user_id);
alter table user_series add constraint fk_user_series_series_7 foreign key (series_id) references series (id) on delete restrict on update restrict;
create index ix_user_series_series_7 on user_series (series_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table comment;

drop table friend;

drop table rating;

drop table series;

drop table user;

drop table user_series;

SET FOREIGN_KEY_CHECKS=1;

