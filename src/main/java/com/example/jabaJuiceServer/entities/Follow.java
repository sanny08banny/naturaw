package com.example.jabaJuiceServer.entities;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.adminDTOs.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne
    @JoinColumn(name = "adminUser_id")
    @JsonIgnore
    private AdminUser adminUser;
    @ManyToOne
    private User followedUser;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "follow_date")
    private Date followDate;

    // Constructors, getters, setters, etc.

    public Follow(Long id, User user, AdminUser adminUser, Product product) {
        this.id = id;
        this.user = user;
        this.adminUser = adminUser;
        this.product = product;
    }

    public Follow(User user, User followedUser) {
        this.user = user;
        this.followedUser = followedUser;
    }

    public Follow(Product product) {
        this.product = product;
    }

    public Follow() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(User followedUser) {
        this.followedUser = followedUser;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }
}
