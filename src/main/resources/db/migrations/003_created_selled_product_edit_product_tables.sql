alter table payment drop column products;
alter table payment drop column amount;
alter table payment drop column discount;
alter table payment add column account_id int references account(id);
alter table payment add column pay_status varchar(50);
alter table payment add column amount_without_Discount decimal(10.2);
alter table payment add column amount_with_Discount decimal(10.2);
alter table payment add column created_date timestamp default now();
alter table product add column category varchar;
alter table product add column quantity int not null default 0;

create table selled_product (
id serial,
payment_id int references payment(id),
name varchar(255) not null,
price decimal(10.2) not null,
discount decimal(5.2),
quantity int not null default 0,
description varchar
);