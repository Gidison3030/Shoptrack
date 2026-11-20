package com.ShopTrack;

import java.io.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileHandler {

    private static final String PRODUCT_FILE = "data/products.csv";
    private static final String SALES_FILE = "data/sales.csv";

    public static void saveProducts(ArrayList<Product> products) {
        new File("data").mkdir();
        try (PrintWriter writer = new PrintWriter(new FileWriter(PRODUCT_FILE))) {
            for (Product p : products) {
                String name = p.getName().replace(",", "");
                if (p instanceof PerishableProduct) {
                    PerishableProduct pp = (PerishableProduct) p;
                    writer.println("P," + name + "," + p.getPrice() + "," + p.getStock() + ","
                            + pp.getExpiryDate());
                } else {
                    writer.println("N," + name + "," + p.getPrice() + "," + p.getStock());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not save products: " + e.getMessage());
        }
    }

    public static ArrayList<Product> loadProducts() {
        ArrayList<Product> list = new ArrayList<>();
        File file = new File(PRODUCT_FILE);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int Stock = Integer.parseInt(parts[3]);
                    if (parts[0].equals("P")) {
                        list.add(new PerishableProduct(name, price, Stock, parts[4]));
                    } else {
                        list.add(new Product(name, price, Stock));
                    }
                } catch (Exception e) {
                    System.out.println("Skipped a bad line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load sales: " + e.getMessage());
        }
        return list;
    }

    public static void saveSales(ArrayList<Sale> sales) {
        new File("data").mkdir();
        try (PrintWriter writer = new PrintWriter(new FileWriter(SALES_FILE))) {
            for (Sale s : sales) {
                String name = s.getProductName().replace(",", ",");
                writer.println(s.getDate() + "," + name + "," + s.getQuantity() + "," +
                        s.getTotal());
            }
        } catch (IOException e) {
            System.out.println("Could not save sales: " + e.getMessage());
        }
    }


    public static ArrayList<Sale> loadSales() {
        ArrayList<Sale> list = new ArrayList<>();
        File file = new File(SALES_FILE);
        if (!file.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    LocalDate date = LocalDate.parse(parts[0]);
                    String name = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    double total = Double.parseDouble(parts[3]);
                    list.add(new Sale(name, quantity, total, date));
                } catch (Exception e) {
                    System.out.println("Skipped a bad sale line: " + e.getMessage());
                }

            }
        } catch (IOException e) {
            System.out.println("Could not load sales: " + e.getMessage());
        }
        return list;
    }



}