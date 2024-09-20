package com.example.jabaJuiceServer.adminDTOs;

import com.example.jabaJuiceServer.entities.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String category;
    private String price,quantity;
    private String highlight;
    @Transient
    private UserDTO userDTO;
    private String datePosted,location;
    private String filePath,fileName;
    @Transient
    private String isLiked,isRelated;

    public ProductDTO(Long id, String name, String category, String price, String highlight, UserDTO userDTO, String datePosted,
                      String location, String filePath, String fileName, String isLiked, String isRelated) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.highlight = highlight;
        this.userDTO = userDTO;
        this.datePosted = datePosted;
        this.location = location;
        this.filePath = filePath;
        this.fileName = fileName;
        this.isLiked = isLiked;
        this.isRelated = isRelated;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public String getIsRelated() {
        return isRelated;
    }

    public void setIsRelated(String isRelated) {
        this.isRelated = isRelated;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    // Constructors, getters, and setters
    // ...
}
