CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

CREATE TABLE IF NOT EXISTS products (
 id INT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(100),
 category VARCHAR(50),
 price DOUBLE,
 description VARCHAR(500),
 image VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users (
 id INT PRIMARY KEY AUTO_INCREMENT,
 username VARCHAR(100) UNIQUE,
 password VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS cart (
 id INT PRIMARY KEY AUTO_INCREMENT,
 user_id INT,
 product_id INT,
 quantity INT
);

CREATE TABLE IF NOT EXISTS orders (
 id INT PRIMARY KEY AUTO_INCREMENT,
 user_id INT,
 total DOUBLE,
 order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO products (name,category,price,description,image)
SELECT * FROM (
 SELECT 'Cotton T-Shirt','Clothing',499.0,'Comfortable cotton t-shirt','tshirt.jpg'
 UNION ALL SELECT 'Casual Shirt','Clothing',799.0,'Stylish casual shirt','shirt.jpg'
 UNION ALL SELECT 'Running Shoes','Footwear',1499.0,'Comfortable running shoes','shoes.jpg'
 UNION ALL SELECT 'Backpack','Bags',899.0,'Lightweight everyday backpack','bag.jpg'
 UNION ALL SELECT 'Watch','Accessories',999.0,'Simple stylish watch','watch.jpg'
) AS x
WHERE NOT EXISTS (SELECT 1 FROM products);
