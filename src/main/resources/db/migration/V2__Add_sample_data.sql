INSERT INTO stores (name) VALUES
('99 Ranch'),
('Vons'),
('Trader Joe''s');

INSERT INTO products (name, category) VALUES
('Banana', 'Fruit'),
('Spinach', 'Vegetable'),
('Bok Choy', 'Vegetable'),
('Milk', 'Dairy'),
('Sourdough Bread', 'Bakery');

INSERT INTO prices (product_id, store_id, price, date) VALUES
(4, 1, 4.99, '2025-12-27'),
(2, 3, 2.49, '2025-05-28'),
(3, 1, 4.49, '2025-05-28'),
(1, 3, 0.69, '2024-01-17'),
(1, 1, 0.69, '2024-01-17'),
(5, 2, 4.99, '2024-01-17'),
(2, 3, 0.79, '2024-01-17');