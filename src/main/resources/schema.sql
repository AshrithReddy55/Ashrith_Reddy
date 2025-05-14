CREATE DATABASE bookstore_db;
USE bookstore_db;

CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    author VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0
);