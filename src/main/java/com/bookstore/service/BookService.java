package com.bookstore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
    
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
    
    public boolean updateBookQuantity(Long id, int quantityToSubtract) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            
            // Check if we have enough books in stock
            if (book.getQuantity() >= quantityToSubtract) {
                book.setQuantity(book.getQuantity() - quantityToSubtract);
                bookRepository.save(book);
                return true;
            }
        }
        
        return false;
    }
}