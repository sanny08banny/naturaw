package com.example.jabaJuiceServer.entities;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.adminDTOs.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "related_items")
public class RelatedItem {
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
    @JoinColumn(name = "stori_ya_jaba_item_id")
    @JsonIgnore
    private StoriYaJabaItem storiYaJabaItem;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    @JsonIgnore
    private Comment comment;

    // Constructors, getters, setters, etc.


    public RelatedItem() {
    }

    public RelatedItem(Long id, User user, AdminUser adminUser, StoriYaJabaItem storiYaJabaItem, Product product) {
        this.id = id;
        this.user = user;
        this.adminUser = adminUser;
        this.storiYaJabaItem = storiYaJabaItem;
        this.product = product;
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

    public StoriYaJabaItem getStoriYaJabaItem() {
        return storiYaJabaItem;
    }

    public void setStoriYaJabaItem(StoriYaJabaItem storiYaJabaItem) {
        this.storiYaJabaItem = storiYaJabaItem;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}

