package com.example.jabaJuiceServer.entities;

public class ProductDTO {
    private String name;
    private String category;
    private String price;
    private byte[] imageBase64;

    public ProductDTO() {
    }

    public ProductDTO(String name, String category, String price, byte[] imageBase64) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageBase64 = imageBase64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public byte[] getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(byte[] imageBase64) {
        this.imageBase64 = imageBase64;
    }
// Constructors, getters, and setters
}