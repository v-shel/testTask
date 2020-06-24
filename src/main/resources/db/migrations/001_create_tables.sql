drop table if exists user cascade;
drop table if exists account cascade;
drop table if exists product cascade;
drop table if exists discount cascade;
drop table if exists payment cascade;

create table user (
id serial,
name varchar(50) not null,
email varchar(50) not null,
is_deleted boolean default false
);

create table account(
id serial,
user_id int not null references user(id),
money decimal(10,2) default '0.0',
is_active boolean default true,
is_deleted boolean default false
);


create table product(
id serial,
name varchar(255) not null,
description varchar,
price decimal(10,2) not null,
is_deleted boolean default false
);

create table discount(
id serial,
product_id int not null references product(id),
percent decimal(5,2) not null,
is_deleted boolean default false
);

create table payment(
id serial,
products varchar not null,
amount decimal(10,2),
discount decimal(10,2),
is_deleted boolean default false
);