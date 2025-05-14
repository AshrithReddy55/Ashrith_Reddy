package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bookstore.model.Book;
import com.bookstore.model.ShoppingCart;
import com.bookstore.service.BookService;

import jakarta.servlet.http.HttpSession;

@Controller
public class StoreController {
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private ShoppingCart cart;
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "store/home";
    }
    
    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam(defaultValue = "1") int quantity, 
                           RedirectAttributes redirectAttributes) {
        bookService.getBookById(id).ifPresent(book -> {
            if (book.getQuantity() >= quantity) {
                cart.addItem(book, quantity);
                redirectAttributes.addFlashAttribute("success", "Book added to cart!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Not enough books in stock!");
            }
        });
        
        return "redirect:/";
    }
    
    @GetMapping("/cart")
    public String viewCart(Model model) {
        // Explicitly add cart to model
        model.addAttribute("cart", cart);
        return "store/cart";
    }
    
    @PostMapping("/cart/update/{id}")
    public String updateCart(@PathVariable Long id, @RequestParam int quantity) {
        if (quantity > 0) {
            cart.updateItem(id, quantity);
        } else {
            cart.removeItem(id);
        }
        return "redirect:/cart";
    }
    
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cart.removeItem(id);
        return "redirect:/cart";
    }
    
    @GetMapping("/checkout")
    public String checkout(Model model) {
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        return "store/checkout";
    }
    
    @PostMapping("/process-payment")
    public String processPayment(RedirectAttributes redirectAttributes) {
        // Process the order by updating inventory
        boolean allItemsProcessed = true;
        
        for (var item : cart.getItems()) {
            Book book = item.getBook();
            int quantity = item.getQuantity();
            
            boolean updated = bookService.updateBookQuantity(book.getId(), quantity);
            if (!updated) {
                allItemsProcessed = false;
                redirectAttributes.addFlashAttribute("error", 
                    "Could not process " + book.getName() + ". Not enough in stock.");
            }
        }
        
        if (allItemsProcessed) {
            // Clear the cart after successful checkout
            cart.clear();
            return "redirect:/payment-success";
        } else {
            return "redirect:/cart";
        }
    }
    
    @GetMapping("/payment-success")
    public String paymentSuccess() {
        return "store/payment-success";
    }
}