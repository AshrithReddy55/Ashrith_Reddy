package com.bookstore.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;

@Controller
@RequestMapping("/admin")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public String adminDashboard(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("book", new Book());
        return "admin/dashboard";
    }
    
    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("success", "Book added successfully!");
        return "redirect:/admin";
    }
    
    @GetMapping("/books/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "admin/edit-book";
        }
        return "redirect:/admin";
    }
    
    @PostMapping("/books/update")
    public String updateBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        bookService.saveBook(book);
        redirectAttributes.addFlashAttribute("success", "Book updated successfully!");
        return "redirect:/admin";
    }
    
    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("success", "Book deleted successfully!");
        return "redirect:/admin";
    }
}