--CREATE DATABASE IF NOT EXISTS `testdb` /*!40100 DEFAULT CHARACTER SET latin1 */;
--USE `testdb`;
--
---- Create the users table
--CREATE TABLE IF NOT EXISTS users (
--    id BIGINT AUTO_INCREMENT PRIMARY KEY,
--    username VARCHAR(50) NOT NULL UNIQUE,
--    password VARCHAR(255) NOT NULL,
--    role VARCHAR(20) NOT NULL
--);
USE `PUBLIC`;
-- Insert an admin user
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$3j4gWBkKx9OHXp4c1O4auOQtF39c3aCtpRQisM9BzS1i4LENbKVkm', 'ADMIN'),
('customer', '$2a$10$xZavTb0i6.IeQ7rEKjKFJ.x8Lq0Tr9l2qLzv/qWYwoWpGykwx4/k.', 'CUSTOMER');

-- ========== Products ==========
INSERT INTO product (name, price) VALUES
('Macbook', 1200.00),
('Phone',  800.00),
('Tablet', 300.00);

-- ========== Discounts ==========
INSERT INTO discount (type, quantity_required, free_quantity, percentage) VALUES
('BUY_N_GET_M_FREE', 10, 5, 20.00),
('BUY_N_GET_PERCENT_OFF', 5, 0, 10.00),
('BUY_N_GET_M_FREE', 20, 2, 33.33);

-- ========== Product-Discount Relationships ==========
INSERT INTO product_discount (product_id, discount_id) VALUES
(1, 1),  -- Laptop -> BUY_N_GET_M_FREE
(2, 2),  -- Laptop -> BUY_N_GET_PERCENT_OFF
(3, 1);  -- Phone -> BUY_N_GET_M_FREE