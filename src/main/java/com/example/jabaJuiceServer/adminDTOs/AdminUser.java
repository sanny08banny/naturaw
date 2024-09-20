package com.example.jabaJuiceServer.adminDTOs;


import com.example.jabaJuiceServer.entities.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "admin_users")
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName, fullName, email, password, role, gender, address;
    private int age;
    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL)
    @JsonManagedReference("adminUser-storiYaJabaItems")
    private List<StoriYaJabaItem> storiYaJabaItems;
    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL)
    @JsonManagedReference("adminUser-deliveryOptions")
    private List<DeliveryOption> deliveryOptions;
    private String filePath;
    private String dateCreated, fcmToken;
    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Follow> follows;

    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Follow> followers;
    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL)
    @JsonManagedReference("adminUser-products")
    private List<Product> products;
    @OneToMany(mappedBy = "adminUser")
    @JsonIgnore
    private List<LikedItem> likedItems;

    @OneToMany(mappedBy = "adminUser")
    @JsonIgnore
    private List<RelatedItem> relatedItems;
    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL)
    @JsonManagedReference("adminUser-offers")
    private List<Offer> offers;
    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL)
    @JsonManagedReference("adminUser-payLinks")
    private List<PayLink> payLinks;

    // Constructors, getters, and setters
    // ...
    public AdminUser() {
    }

    public AdminUser(Long id, String userName, String fullName, String email,
                     String password, String role, String gender, String address,
                     int age, List<StoriYaJabaItem> storiYaJabaItems, List<DeliveryOption> deliveryOptions,
                     String filePath, String dateCreated,
                     List<Follow> follows, List<Follow> followers, List<Product> products,
                     List<LikedItem> likedItems, List<RelatedItem> relatedItems, List<PayLink> paymentMethods) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.address = address;
        this.age = age;
        this.storiYaJabaItems = storiYaJabaItems;
        this.deliveryOptions = deliveryOptions;
        this.filePath = filePath;
        this.dateCreated = dateCreated;
        this.follows = follows;
        this.followers = followers;
        this.products = products;
        this.likedItems = likedItems;
        this.relatedItems = relatedItems;
        this.payLinks = paymentMethods;
    }

    public List<Follow> getFollows() {
        return follows;
    }

    public void setFollows(List<Follow> follows) {
        this.follows = follows;
    }

    public List<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follow> followers) {
        this.followers = followers;
    }

    public List<StoriYaJabaItem> getStoriYaJabaItems() {
        return storiYaJabaItems;
    }

    public void setStoriYaJabaItems(List<StoriYaJabaItem> storiYaJabaItems) {
        this.storiYaJabaItems = storiYaJabaItems;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<LikedItem> getLikedItems() {
        return likedItems;
    }

    public void setLikedItems(List<LikedItem> likedItems) {
        this.likedItems = likedItems;
    }

    public List<RelatedItem> getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(List<RelatedItem> relatedItems) {
        this.relatedItems = relatedItems;
    }

    public List<DeliveryOption> getDeliveryOptions() {
        return deliveryOptions;
    }

    public void setDeliveryOptions(List<DeliveryOption> deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<PayLink> getPayLinks() {
        return payLinks;
    }

    public void setPayLinks(List<PayLink> payLinks) {
        this.payLinks = payLinks;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public AdminUser createAdminItem(User user) {
        AdminUser adminUser = new AdminUser();

// Copy attributes from the User to AdminUser
        adminUser.setUserName(user.getUserName());
        adminUser.setFullName(user.getFullName());
        adminUser.setEmail(user.getEmail());
        adminUser.setPassword(user.getPassword());
        adminUser.setGender(user.getGender());
        adminUser.setAddress(user.getAddress());
        adminUser.setAge(user.getAge());
        adminUser.setFilePath(user.getFilePath());
        adminUser.setDateCreated(user.getDateCreated());
        adminUser.setFcmToken(user.getFcmToken());

// Set the role to "ADMIN"
        adminUser.setRole("ADMIN");
        return adminUser;
    }
}