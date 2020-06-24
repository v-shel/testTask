insert into discount (id, percent, description)
values (1, '20.00', 'First'),
(2, '17.00', 'Second'),
(3, '31.00', 'Third'),
(4, '45.00', 'Fourth');

insert into product (id, discount_id, name, description, price, quantity)
values (1, 1, 'First product', 'Super product from Ukraine!', '100.00', 1000),
(2, null, 'Tapki', 'Ukranian tapki!', '200.00', 1000),
(3, 2, 'Mouse', 'Extra convinient game mouse!', '400.00', 1000),
(4, 3, 'Ball', 'Air ball for kids', '250.00', 1000),
(5, null, 'Pool', 'Water pool for garden.', '600.00', 1000),
(6, 4, 'Bicycle', 'Electro bicycle. The product of china.', '1000.00', 1000);

insert into user (id, name, email)
values (1, 'Jimm lou', 'jimm.lou@email.com');

insert into account (id, user_id, money)
values (1, 1, '200000.00');