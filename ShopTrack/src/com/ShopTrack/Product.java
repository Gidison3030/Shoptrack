package com.ShopTrack;

public class Product implements Reportable {
    private String Name;
    private double Price;
    private int Stock;

    public Product(String name, double price, int stock) {
        this.Name = name;
        this.Price = price;
        this.Stock = stock;
    }

public String getName() {
        return Name;
}

public double getPrice() {
        return Price;
}

public int getStock() {
        return Stock;
    }


public void setPrice(double price) {
        if (price < 0) {
            System.out.println("Price cannot be negative");
        }else{
            this.Price = price;
        }
    }

    public void setStock(int stock) {
        if (stock < 0) {
            System.out.println("Stock cannot be negative");
        }else{
            this.Stock = stock;
        }
    }

public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty");
        }else {
            this.Name = name.trim();
        }
}

public void reduceStock(int quantity) throws OutOfStockException {
        if (quantity > Stock) {
            throw new OutOfStockException("Only " + Stock + " of " + Name + " left.");
        }
        Stock = Stock -  quantity ;
}

@Override
public String getReportLine() {
        double totalValue = Price * Stock;
        return  Name + " - Stock value: GHS " + totalValue;
}


@Override
public String toString() {
        return Name + " | GHs " + Price + " | Stock " + Stock;
}

}
