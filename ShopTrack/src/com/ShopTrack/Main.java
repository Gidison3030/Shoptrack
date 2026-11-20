package com.ShopTrack;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean running = true;
        Repository<Product> productRepo = new Repository<>(FileHandler.loadProducts());
        Repository<Sale> saleRepo = new Repository<>(FileHandler.loadSales());

        ArrayList<Product> products = productRepo.getAll();
        ArrayList<Sale> sales = saleRepo.getAll();

        HashMap<String, Product> lookup = new HashMap<>();
        for (Product p : products) {
            lookup.put(p.getName().toLowerCase(), p);
        }

        while (running) {
            System.out.println("\n====SHOPTRACK=====");
            System.out.println("1. Add product");
            System.out.println("2. view products");
            System.out.println("3. Search products");
            System.out.println("4. Update product");
            System.out.println("5. Delete product");
            System.out.println("6. Record sale");
            System.out.println("7. View all sales");
            System.out.println("8. Low stock alert");
            System.out.println("9. Daily sales report");
            System.out.println("10. Exit");

            int choice = readInt(sc, "Enter your option: ");

            switch (choice) {
                case 1: {
                    String itemName = readString(sc, "Enter item name: ");
                    if (lookup.containsKey(itemName.toLowerCase())) {
                        System.out.println("That product already exists!");
                        break;
                    }

                    double Price = readDouble(sc, "Enter price ");

                    int Stock = readInt(sc, "Enter product stock:");

                    Product p;
                    int type = readInt(sc, "Type (1 = Normal, 2 = Perishable): ");

                    if (type == 2) {
                        String expiry = readString(sc, "Enter expiry date (e.g. 2026-12-31): ");
                        p = new PerishableProduct(itemName, Price, Stock, expiry);
                    } else {
                        p = new Product(itemName, Price, Stock);
                    }

                    productRepo.add(p);
                    lookup.put(p.getName().toLowerCase(), p);
                    System.out.println(p);
                    System.out.println("Product added.");
                    break;
                }
                case 2: {
                    if (products.isEmpty()) {
                        System.out.println("No product yet");
                    } else {
                        for (Product item : products) {
                            System.out.println(item);
                            System.out.println("  " + item.getReportLine());
                        }
                        System.out.println("Press enter to continue...");
                        sc.nextLine();
                    }
                    break;
                }

                case 3: {
                    String Keyword = readString(sc, "Enter name to search: ");
                    boolean found = false;
                    for (Product item : products) {
                        if (item.getName().toLowerCase().contains(Keyword.toLowerCase())) {
                            System.out.println(item);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("No product found.");
                    }
                    break;
                }
                case 4: {
                    String searchName = readString(sc, "Enter exact name of the product to update: ");
                    Product target = lookup.get(searchName.toLowerCase());

                    if (target == null) {
                        System.out.println("Product not found.");
                    } else {
                        System.out.println("Current: " + target);
                        double newPrice = readDouble(sc, "Enter new price: ");
                        int newStock = readInt(sc, "Enter new stock: ");
                        target.setPrice(newPrice);
                        target.setStock(newStock);
                        System.out.println("Updated: " + target);
                    }
                    break;

                }

                case 5: {
                    String deleteName = readString(sc, "Enter the name of the product to delete: ");
                    Product target = lookup.get(deleteName.toLowerCase());

                    for (Product item : products) {
                        if (item.getName().equalsIgnoreCase(deleteName)) {
                            target = item;
                        }
                    }

                    if (target == null) {
                        System.out.println("Product not found.");
                    } else {
                        System.out.println("Found: " + target);
                        String answer = readString(sc, "Delete this product? (Yes/No): ");
                        if (answer.equalsIgnoreCase("Yes")) {
                            productRepo.remove(target);
                            lookup.remove(target.getName().toLowerCase());
                            System.out.println("Product deleted.");
                        } else {
                            System.out.println("Delete failed.");
                        }
                        break;
                    }
                }

                case 6: {
                    String saleName = readString(sc, "Enter name of product to sell: ");
                    Product target = lookup.get(saleName.toLowerCase());

                    for (Product item : products) {
                        if (item.getName().equalsIgnoreCase(saleName)) {
                            target = item;
                        }
                    }

                    if (target == null) {
                        System.out.println("Product not found.");
                    } else {
                        int Qty = readInt(sc, "Enter quantity to sell: ");
                        if (Qty == 0) {
                            System.out.println("Quantity must be at least one.");
                        } else {
                            try {
                                target.reduceStock(Qty);
                                double total = target.getPrice() * Qty;
                                saleRepo.add(new Sale(target.getName(), Qty, total));
                                System.out.println("\n====== RECEIPT ======");
                                System.out.println("Item:     " + target.getName());
                                System.out.println("Quantity: " + Qty);
                                System.out.println("Price:    GHS " + target.getPrice());
                                System.out.println("Total:    GHS" + total);
                                System.out.println("===========================");
                            } catch (OutOfStockException e) {
                                System.out.println("Sale failed: " + e.getMessage());
                            }
                        }
                    }
                    break;
                }
                case 7: {
                    if (sales.isEmpty()) {
                        System.out.println("No sales yet");
                    } else {
                        double grandTotal = 0;
                        for (Sale s : sales) {
                            System.out.println(s);
                            grandTotal = grandTotal + s.getTotal();
                        }
                        System.out.println("Total sales: GhS " + grandTotal);
                    }
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    break;
                }
                case 8: {
                    int limit = readInt(sc, "Show products with stock below: ");

                    List<Product> lowStock = products.stream().filter(item -> item.getStock() <= limit).toList();

                    if (lowStock.isEmpty()) {
                        System.out.println("No product found below " + limit + ",");
                    } else {
                        System.out.println("LOW STOCK ALERT");
                        for (Product item : lowStock) {
                            System.out.println(item);
                        }
                    }
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    break;
                }

                case 9: {
                    LocalDate today = LocalDate.now();

                    List<Sale> todaySales = sales.stream().filter(s -> s.getDate().equals(today)).toList();

                    if (todaySales.isEmpty()) {
                        System.out.println("No sales today.");
                    } else {
                        double totalMoney = todaySales.stream().mapToDouble(s -> s.getTotal()).sum();

                        int totalItems = todaySales.stream().mapToInt(s -> s.getQuantity()).sum();

                        Map<String, Integer> perProduct = todaySales.stream().collect(Collectors.groupingBy(s -> s.getProductName(), Collectors.summingInt(s -> s.getQuantity())));

                        String bestSeller = perProduct.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();

                        System.out.println("\n===== DAILY REPORT: " + today + " =====");
                        System.out.println("Number of sales: " + todaySales.size());
                        System.out.println("Item sold:       " + totalItems);
                        System.out.println("Total sales:     GHS " + totalMoney);
                        System.out.println("Best seller:     " + bestSeller + " (" + perProduct.get(bestSeller) + " sold)");
                        System.out.println("========================================");
                    }
                    System.out.println("Press enter to continue...");
                    sc.nextLine();
                    break;
                }


                case 10: {
                    FileHandler.saveProducts(products);
                    FileHandler.saveSales(sales);
                    System.out.println("Data saved. Goodbye");
                    running = false;
                    break;
                }

                default: {
                    System.out.println("Invalid option");
                }
            }
        }

    }

    public static String readString(Scanner sc, String message) {
        while (true) {
            System.out.println(message);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    public static double readDouble(Scanner sc, String message) {
        while (true) {
            System.out.println(message);
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid price.");
            }
        }
    }

    public static int readInt(Scanner sc, String Message) {
        while (true) {
            System.out.println(Message);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number");
            }

        }
    }
}



