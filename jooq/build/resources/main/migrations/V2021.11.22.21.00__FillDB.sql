INSERT INTO COMPANY (NAME, INDIVIDUAL_TAX_NUMBER, COMPANY_CHECK)
VALUES
('COMPANY 1', 'COMPANY 1 ITN', 'COMPANY 1 CHECK'),
('COMPANY 2', 'COMPANY 2 ITN', 'COMPANY 2 CHECK'),
('COMPANY 3', 'COMPANY 3 ITN', 'COMPANY 3 CHECK'),
('COMPANY 4', 'COMPANY 4 ITN', 'COMPANY 4 CHECK'),
('COMPANY 5', 'COMPANY 5 ITN', 'COMPANY 5 CHECK');

INSERT INTO WAYBILL (DATE, COMPANY_ID)
VALUES
('20211121', 1),
('20211121', 1),
('20211121', 1),
('20211121', 2),
('20211121', 2),
('20211121', 2),
('20211121', 3),
('20211121', 4),
('20211121', 5);

INSERT INTO PRODUCT (NAME, INNER_CODE)
VALUES
('Keks', 'CODE 1'),
('Ukrop', 'CODE 2'),
('Prod-replay', 'CODE 3'),
('PRODUCT 4', 'CODE 4'),
('PRODUCT 5', 'CODE 5'),
('PRODUCT 6', 'CODE 6'),
('PRODUCT 7', 'CODE 7'),
('PRODUCT 8', 'CODE 8'),
('PRODUCT 9', 'CODE 9');

INSERT INTO WAYBILL_PRODUCTS (WAYBILL_ID, PRODUCT_ID, PRICE, COUNT)
VALUES
(1, 1, 11, 1),
(1, 2, 12, 2),
(1, 5, 13, 3),
(2, 1, 14, 4),
(2, 5, 15, 5),
(3, 6, 16, 6),
(4, 5, 17, 7),
(5, 5, 18, 8),
(5, 6, 19, 9),
(6, 1, 110, 10),
(7, 7, 111, 11),
(9, 2, 112, 12),
(9, 3, 113, 13);