--CREATE DATABASE IF NOT EXISTS `testdb` /*!40100 DEFAULT CHARACTER SET latin1 */;
--USE `testdb`;
--

-- Clean up existing data
DROP TABLE IF EXISTS product_discount;
DROP TABLE IF EXISTS discounts;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
-- Create the users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);
-- Create the product table
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    price DECIMAL(10,2) NOT NULL
);
-- Create the discount table
CREATE TABLE IF NOT EXISTS discount (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  type VARCHAR(255) NOT NULL,
  quantity_required INT NOT NULL,
  percentage DECIMAL(5,2) NOT NULL
);
-- Create the product_discount table
CREATE TABLE IF NOT EXISTS product_discount (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    discount_id BIGINT NOT NULL,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT fk_discount FOREIGN KEY (discount_id) REFERENCES discount(id)
);