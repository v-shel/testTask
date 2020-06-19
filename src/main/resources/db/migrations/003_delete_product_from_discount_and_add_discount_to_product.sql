alter table discount drop column product_id;
alter table product add column discount_id int references discount(id);
