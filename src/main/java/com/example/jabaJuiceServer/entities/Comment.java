package com.example.jabaJuiceServer.entities;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.adminDTOs.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stori_ya_jaba_item_id")
    @JsonIgnore
    private StoriYaJabaItem storiYaJabaItem;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @Transient
    private UserDTO userDTO;
    @ManyToOne
    @JoinColumn(name = "adminUser_id")
    @JsonIgnore
    private AdminUser adminUser;
    @OneToMany(mappedBy = "comment")
    @JsonIgnore
    private Set<LikedItem> likedItems = new HashSet<>();

    @OneToMany(mappedBy = "comment")
    @JsonIgnore
    private Set<RelatedItem> relatedItems = new HashSet<>();

    public Comment() {
    }

    public Comment(String text) {
        this.text = text;
    }

    public Comment(Long id, String text, StoriYaJabaItem storiYaJabaItem, Product product, User user, AdminUser adminUser) {
        this.id = id;
        this.text = text;
        this.storiYaJabaItem = storiYaJabaItem;
        this.product = product;
        this.user = user;
        this.adminUser = adminUser;
    }
    public UserDTO getUserDTO() {
        if (user != null){
            return new UserDTO(user.getId(), user.getUserName(), user.getFullName(),
                    user.getEmail(), user.getRole(), user.getGender(), user.getAddress(), user.getAge(),
                    user.getFilePath(), user.getDateCreated());
        }else {
            return userDTO;
        }
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public StoriYaJabaItem getStoriYaJabaItem() {
        return storiYaJabaItem;
    }

    public void setStoriYaJabaItem(StoriYaJabaItem storiYaJabaItem) {
        this.storiYaJabaItem = storiYaJabaItem;
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

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
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
// Constructors, getters, setters, and other methods
}
