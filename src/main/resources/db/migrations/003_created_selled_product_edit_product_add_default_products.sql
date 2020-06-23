alter table payment drop column products;
alter table payment drop column amount;
alter table payment drop column discount;
alter table payment add column account_id int references account(id);
alter table payment add column amount_without_Discount decimal(10.2);
alter table payment add column amount_with_Discount decimal(10.2);
alter table payment add column created_date timestamp default now();

alter table product add column category varchar;

create table selled_product (
id serial,
payment_id int references payment(id),
name varchar(255) not null,
price decimal(10.2) not null,
discount decimal(5.2),
description varchar
);

insert into product (id, name, description, price)
values (1, 'First product', 'Super product from Ukraine!', '100.00'),
(2, 'Tapki', 'Ukranian tapki!', '200.00'),
(3, 'Mouse', 'Extra convinient game mouse!', '400.00'),
(4, 'Ball', 'Air ball for kids', '250.00'),
(5, 'Pool', 'Water pool for garden.', '600.00'),
(6, 'Bicycle', 'Electro bicycle. The product of china.', '1000.00');

