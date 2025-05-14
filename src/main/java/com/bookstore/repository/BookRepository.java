package com.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Spring Data JPA provides basic CRUD operations by default
    // You can add custom query methods if needed
}