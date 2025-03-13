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
VALUES ('admin', '$2a$10$3j4gWBkKx9OHXp4c1O4auOQtF39c3aCtpRQisM9BzS1i4LENbKVkm', 'ADMIN');

-- Insert a customer user
INSERT INTO users (username, password, role)
VALUES ('customer', '$2a$10$xZavTb0i6.IeQ7rEKjKFJ.x8Lq0Tr9l2qLzv/qWYwoWpGykwx4/k.', 'CUSTOMER');