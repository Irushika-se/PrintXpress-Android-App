package com.example.printxpress.models;

public class PrintProduct {
    private int productId;
    private String name, category, description;
    private double price;
    private String imageRes;

    public PrintProduct() {}

    public PrintProduct(int productId, String name, String category, String description, double price) {
        this.productId = productId; this.name = name; this.category = category;
        this.description = description; this.price = price;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageRes() { return imageRes; }
    public void setImageRes(String imageRes) { this.imageRes = imageRes; }
}
