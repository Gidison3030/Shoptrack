package com.ShopTrack;

public class PerishableProduct extends Product {
    private String expiryDate;

    public PerishableProduct(String Name, double Price, int Stock, String expiryDate) {
        super(Name, Price, Stock);
        this.expiryDate = expiryDate;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    @Override
    public String toString() {
        return super.toString() + " | Expires: " +  expiryDate;
    }

    @Override
    public String getReportLine() {
        return super.getReportLine() + " (Expires: " + expiryDate + ")";
    }
}
