package com.bookstore.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {
    
    private List<CartItem> items = new ArrayList<>();
    
    public void addItem(Book book, int quantity) {
        // Check if the book is already in the cart
        for (CartItem item : items) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        
        // If not in cart, add new item
        items.add(new CartItem(book, quantity));
    }
    
    public void updateItem(Long bookId, int quantity) {
        for (CartItem item : items) {
            if (item.getBook().getId().equals(bookId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }
    
    public void removeItem(Long bookId) {
        items.removeIf(item -> item.getBook().getId().equals(bookId));
    }
    
    public List<CartItem> getItems() {
        return items;
    }
    
    public void clear() {
        items.clear();
    }
    
    public double getTotal() {
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }
    
    public int getItemCount() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}