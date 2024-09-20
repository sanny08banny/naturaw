package com.example.jabaJuiceServer.adminDTOs;

import com.example.jabaJuiceServer.entities.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sanny_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private String price,quantity;
    private String highlight;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("product")
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-products")
    private User user;
    @Transient
    private UserDTO userDTO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id")
    @JsonBackReference("adminUser-products")
    private AdminUser adminUser;
    private String datePosted,location;
    private String filePath,fileName;
    //Ui - Unique Identifier
    private String productLine,UI;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference("order-products")
    private Order order;
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<LikedItem> likedItems;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<RelatedItem> relatedItems;
    @Transient
    private String isLiked = "false",isRelated = "false", isFollowing = "false";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    @JsonBackReference("offer-products")
    private Offer offer;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference("product-payLinks")
    private Set<PayLink> payLinks;

    public Product() {
    }


    public Product(Long id, String name, String category,
                   String price, String highlight, List<Comment> comments, User user,
                   AdminUser adminUser, String datePosted, String location, String filePath, String fileName, Order order) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.highlight = highlight;
        this.comments = comments;
        this.user = user;
        this.adminUser = adminUser;
        this.datePosted = datePosted;
        this.location = location;
        this.filePath = filePath;
        this.fileName = fileName;
        this.order = order;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Set<PayLink> getPayLinks() {
        return payLinks;
    }

    public void setPayLinks(Set<PayLink> payLinks) {
        this.payLinks = payLinks;
    }

    public String getProductLine() {
        return productLine;
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }

    public String getUI() {
        return UI;
    }

    public void setUI(String UI) {
        this.UI = UI;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(String isFollowing) {
        this.isFollowing = isFollowing;
    }
    // Constructors, getters, and setters
    // ...
}
