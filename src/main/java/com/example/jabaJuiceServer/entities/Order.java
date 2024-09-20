package com.example.jabaJuiceServer.entities;

import com.example.jabaJuiceServer.MyDAOs.OrderProductMap;
import com.example.jabaJuiceServer.adminDTOs.DeliveryOption;
import com.example.jabaJuiceServer.adminDTOs.DeliveryOptionDTO;
import com.example.jabaJuiceServer.adminDTOs.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status,confirmationStatus;
    private double totalPrice, deliveryPrice, productPrice;
    private String orderType, timeStamp, dateTimePosted, orderNumber,selectedLocation;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference("order-products")
    private Set<Product> products = new HashSet<>();
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference("order-orderProductMaps")
    private List<OrderProductMap> orderProductMaps;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-orders")
    private User user;
    @Transient
    private UserDTO userDTO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_option_id")
    @JsonBackReference("deliveryOption-orders")
    private DeliveryOption deliveryOption;
    @Transient
    private DeliveryOptionDTO deliveryOptionDTO;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference("order-transaction")
    private Transaction transaction;

    public Order(String confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
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

    public DeliveryOptionDTO getDeliveryOptionDTO() {
        if (deliveryOption != null){
            DeliveryOptionDTO dto = new DeliveryOptionDTO();
            dto.setAvailability(deliveryOption.isAvailability());
            dto.setId(deliveryOption.getId());
            dto.setUserDTO(deliveryOption.getUserDTO());
            dto.setDeliveryType(deliveryOption.getDeliveryType());
            dto.setCharges(deliveryOption.getCharges());
            dto.setLocationsAvailable(deliveryOption.getLocationsAvailable());
            dto.setTimeAvailable(deliveryOption.getTimeAvailable());
            return dto;
        }
        return deliveryOptionDTO;
    }

    public void setDeliveryOptionDTO(DeliveryOptionDTO deliveryOptionDTO) {
        this.deliveryOptionDTO = deliveryOptionDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDateTimePosted() {
        return dateTimePosted;
    }

    public void setDateTimePosted(String dateTimePosted) {
        this.dateTimePosted = dateTimePosted;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeliveryOption getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(DeliveryOption deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public String getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(String confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }

    public List<OrderProductMap> getOrderProductMaps() {
        return orderProductMaps;
    }

    public void setOrderProductMaps(List<OrderProductMap> orderProductMaps) {
        this.orderProductMaps = orderProductMaps;
    }
}
