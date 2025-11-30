use ALLRA;

INSERT INTO ALLRA.members
(id, address, created_at, deleteyn, name, password, updated_at, email)
VALUES(1, '서울특별시 관악구', '2025-11-30 09:20:13.470875', 'N', 'A', '1234!@#', '2025-11-30 09:20:13.470875', 'test1@test.com');
INSERT INTO ALLRA.members
(id, address, created_at, deleteyn, name, password, updated_at, email)
VALUES(2, '서울특별시 관악구', '2025-11-30 09:24:21.827101', 'N', 'A', '1234!@#', '2025-11-30 09:24:21.827101', 'test2@test.com');



INSERT INTO ALLRA.categories
(id, name)
VALUES(1, '과일');
INSERT INTO ALLRA.categories
(id, name)
VALUES(2, '옷');


INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(176, '2025-11-30 00:14:07.737030', 'N', '사과', 1000, 'IN_STOCK', 1, '2025-11-30 00:14:07.737030', 1);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(177, '2025-11-30 00:14:07.765262', 'N', '바나나', 2000, 'IN_STOCK', 10, '2025-11-30 00:14:07.765262', 1);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(178, '2025-11-30 00:14:07.777786', 'N', '포도', 500, 'SOLD_OUT', 0, '2025-11-30 00:14:07.777786', 1);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(179, '2025-11-30 00:14:07.791457', 'N', '청바지3', 10000, 'IN_STOCK', 10, '2025-11-30 00:14:07.791457', 2);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(180, '2025-11-30 00:14:07.804555', 'N', '스웨터3', 100000, 'SOLD_OUT', 0, '2025-11-30 00:14:07.804555', 2);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(181, '2025-11-30 00:14:08.275278', 'N', '사과3', 1000, 'SOLD_OUT', 0, '2025-11-30 00:14:08.275278', 1);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(182, '2025-11-30 00:14:08.289023', 'N', '바나나3', 2000, 'IN_STOCK', 1, '2025-11-30 00:14:08.289023', 1);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(183, '2025-11-30 00:14:08.304538', 'N', '포도3', 500, 'SOLD_OUT', 0, '2025-11-30 00:14:08.304538', 1);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(184, '2025-11-30 00:14:08.321038', 'N', '청바지2', 10000, 'IN_STOCK', 10, '2025-11-30 00:14:08.321038', 2);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(185, '2025-11-30 00:14:08.335088', 'N', '스웨터2', 100000, 'SOLD_OUT', 0, '2025-11-30 00:14:08.335088', 2);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(186, '2025-11-30 00:14:08.402443', 'N', '사과2', 1000, 'IN_STOCK', 1, '2025-11-30 00:14:08.402443', 1);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(187, '2025-11-30 00:14:08.416056', 'N', '바나나2', 2000, 'IN_STOCK', 2, '2025-11-30 00:14:08.416056', 1);
INSERT INTO ALLRA.products
(id, created_at, deleteyn, name, price, status, stock, updated_at, category_id)
VALUES(188, '2025-11-30 00:14:08.429232', 'N', '포도2', 500, 'SOLD_OUT', 0, '2025-11-30 00:14:08.429232', 1);


INSERT INTO ALLRA.basket
(id, member_id)
VALUES(1, 1);
INSERT INTO ALLRA.basket
(id, member_id)
VALUES(2, 2);