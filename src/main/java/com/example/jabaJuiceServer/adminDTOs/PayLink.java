package com.example.jabaJuiceServer.adminDTOs;

import com.example.jabaJuiceServer.entities.UserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "pay_links")
public class PayLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    private UserDTO userDTO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id")
    @JsonBackReference("adminUser-payLinks")
    private AdminUser adminUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference("product-payLinks")
    private Product product;
    @Transient
    private ProductDTO productDTO;
    private String transactionType,recipientNumber;
    private String shortcode;
    private String paybillNumber;
    private String accountNumber;
    private String pochiRecipientNumber,dateTimeCreated, UI;

    public PayLink() {
    }

    public ProductDTO getProductDTO(){
        if (product != null){
            return new ProductDTO(product.getId(),product.getName(),product.getCategory(),product.getPrice(),product.getHighlight(),product.getUserDTO(),
                    product.getDatePosted(),product.getLocation(),product.getFilePath(),product.getFileName(),product.getIsLiked(),product.getIsRelated());
        }else return productDTO;
    }

    public UserDTO getUserDTO() {
        if (adminUser != null){
            return new UserDTO(adminUser.getId(), adminUser.getUserName(), adminUser.getFullName(),
                    adminUser.getEmail(), adminUser.getRole(), adminUser.getGender(), adminUser.getAddress(), adminUser.getAge(),
                    adminUser.getFilePath(), adminUser.getDateCreated());
        }else {
            return userDTO;
        }
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    public String getPaybillNumber() {
        return paybillNumber;
    }

    public void setPaybillNumber(String paybillNumber) {
        this.paybillNumber = paybillNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPochiRecipientNumber() {
        return pochiRecipientNumber;
    }

    public void setPochiRecipientNumber(String pochiRecipientNumber) {
        this.pochiRecipientNumber = pochiRecipientNumber;
    }

    public String getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(String dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public String getUI() {
        return UI;
    }

    public void setUI(String UI) {
        this.UI = UI;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }
}
