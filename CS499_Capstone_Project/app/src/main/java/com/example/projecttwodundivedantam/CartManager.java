package com.example.projecttwodundivedantam;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private final List<CartItem> items = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public void addItem(String name, double price) {
        for (CartItem item : items) {
            if (item.getName().equals(name)) {
                item.incrementQuantity();
                return;
            }
        }
        items.add(new CartItem(name, price));
    }

    public List<CartItem> getItems() { return items; }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) total += item.getTotal();
        return total;
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem item : items) count += item.getQuantity();
        return count;
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) items.remove(index);
    }

    public void clearCart() { items.clear(); }
}