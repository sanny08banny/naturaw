package com.example.jabaJuiceServer.adminDTOs;

import com.example.jabaJuiceServer.entities.UserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filePath;
    private String description,datePosted,expiryDate;
    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    @JsonManagedReference("offer-products")
    private List<Product> products;
    @Transient
    private UserDTO userDTO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id")
    @JsonBackReference("adminUser-offers")
    private AdminUser adminUser;

    public Offer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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
}
