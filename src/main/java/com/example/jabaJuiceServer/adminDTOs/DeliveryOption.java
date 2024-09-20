package com.example.jabaJuiceServer.adminDTOs;

import com.example.jabaJuiceServer.entities.Order;
import com.example.jabaJuiceServer.entities.UserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "delivery_options")
public class DeliveryOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deliveryType;
    private String timeAvailable;
    private boolean availability;
    private List<String> locationsAvailable;
    private double charges;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_user_id")
    @JsonBackReference("adminUser-deliveryOptions")
    private AdminUser adminUser;
    @Transient
    private UserDTO userDTO;
    @OneToMany(mappedBy = "deliveryOption", cascade = CascadeType.ALL)
    @JsonManagedReference("deliveryOption-orders")
    @JsonIgnore
    private Set<Order> orders = new HashSet<>();


    // Constructors, getters, and setters

    // Default constructor (empty constructor required by JPA)
    public DeliveryOption() {
    }

    // Parameterized constructor
    public DeliveryOption(String deliveryType, String timeAvailable, boolean availability,
                          List<String> locationsAvailable, double charges, AdminUser adminUser, Set<Order> orders) {
        this.deliveryType = deliveryType;
        this.timeAvailable = timeAvailable;
        this.availability = availability;
        this.locationsAvailable = locationsAvailable;
        this.charges = charges;
        this.adminUser = adminUser;
        this.orders = orders;
    }

    // Getters and setters for all fields
    // ...
    public UserDTO getUserDTO() {
        if (adminUser != null){
            return new UserDTO(adminUser.getId(), adminUser.getUserName(), adminUser.getFullName(),
                    adminUser.getEmail(), adminUser.getRole(), adminUser.getGender(), adminUser.getAddress(), adminUser.getAge(),
                    adminUser.getFilePath(), adminUser.getDateCreated(), adminUser.getFcmToken(),"");
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

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getTimeAvailable() {
        return timeAvailable;
    }

    public void setTimeAvailable(String timeAvailable) {
        this.timeAvailable = timeAvailable;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public List<String> getLocationsAvailable() {
        return locationsAvailable;
    }

    public void setLocationsAvailable(List<String> locationsAvailable) {
        this.locationsAvailable = locationsAvailable;
    }

    public double getCharges() {
        return charges;
    }

    public void setCharges(double charges) {
        this.charges = charges;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
    // Additional methods (if needed)

    // ...
}

