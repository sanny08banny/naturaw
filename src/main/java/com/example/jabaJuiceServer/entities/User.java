package com.example.jabaJuiceServer.entities;

import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.analyticsDAOs.Interest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName, fullName, email, password, role, gender, address, accountType;
    private int age;

    // Use HashSet instead of List for these relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-storiYaJabaItems")
    private Set<StoriYaJabaItem> storiYaJabaItems = new HashSet<>();

    private String filePath;
    private String dateCreated,fcmToken;

    // Use HashSet instead of List for these relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Follow> follows = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Follow> followers = new HashSet<>();

    // Use HashSet instead of List for these relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-products")
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<LikedItem> likedItems = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<RelatedItem> relatedItems = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-orders")
    private Set<Order> orders = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-transactions")
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-interests")
    private Set<Interest> interests = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-notifications")
    private Set<Notification> notifications = new HashSet<>();


    public User() {
    }

    public Long getId() {
        return id;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<StoriYaJabaItem> getStoriYaJabaItems() {
        return storiYaJabaItems;
    }

    public void setStoriYaJabaItems(Set<StoriYaJabaItem> storiYaJabaItems) {
        this.storiYaJabaItems = storiYaJabaItems;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<Follow> getFollows() {
        return follows;
    }

    public void setFollows(Set<Follow> follows) {
        this.follows = follows;
    }

    public Set<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<Follow> followers) {
        this.followers = followers;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<LikedItem> getLikedItems() {
        return likedItems;
    }

    public void setLikedItems(Set<LikedItem> likedItems) {
        this.likedItems = likedItems;
    }

    public Set<RelatedItem> getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(Set<RelatedItem> relatedItems) {
        this.relatedItems = relatedItems;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
}
