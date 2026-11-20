package com.ShopTrack;

import java.time.LocalDate;

public class Sale {

    private String productName;
    private int quantity;
    private double total;
    private LocalDate date;

    public Sale(String productName, int quantity, double total) {
        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
        this.date = LocalDate.now();
    }

    public Sale(String productName, int quantity, double total, LocalDate date) {
        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
        this.date = date;
    }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return date + " | " + productName + " x " + quantity + " | GHS " + total;
    }
}
