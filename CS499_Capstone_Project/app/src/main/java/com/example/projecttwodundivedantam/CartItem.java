package com.example.projecttwodundivedantam;

public class CartItem {
    private String name;
    private double price;
    private int quantity;

    public CartItem(String name, double price) {
        this.name     = name;
        this.price    = price;
        this.quantity = 1;
    }

    public String getName()    { return name; }
    public double getPrice()   { return price; }
    public int    getQuantity(){ return quantity; }

    public void incrementQuantity() { quantity++; }

    public double getTotal() { return price * quantity; }

    @Override
    public String toString() {
        return name + " x" + quantity + "  —  $" + String.format("%.2f", getTotal());
    }
}