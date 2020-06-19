alter table discount drop column product_id;
alter table discount add column description varchar(2048);
alter table product add column discount_id int references discount(id);
