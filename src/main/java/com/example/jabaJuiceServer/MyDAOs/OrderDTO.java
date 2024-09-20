package com.example.jabaJuiceServer.MyDAOs;

import com.example.jabaJuiceServer.adminDTOs.DeliveryOption;
import com.example.jabaJuiceServer.adminDTOs.DeliveryOptionDTO;
import com.example.jabaJuiceServer.entities.Transaction;
import com.example.jabaJuiceServer.entities.UserDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderDTO {
    private Long id;
    private String status, confirmationStatus;
    private double totalPrice, deliveryPrice, productPrice;
    private String orderType, timeStamp, dateTimePosted, orderNumber,selectedLocation;
    private List<OrderProductMap> orderProductMaps;
    private UserDTO userDTO;
    private DeliveryOptionDTO deliveryOptionDTO;
    private DeliveryOption deliveryOption;

    public OrderDTO() {
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

    public String getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(String confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
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

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public DeliveryOption getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(DeliveryOption deliveryOption) {
        this.deliveryOption = deliveryOption;
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

    public DeliveryOptionDTO getDeliveryOptionDTO() {
        return deliveryOptionDTO;
    }

    public void setDeliveryOptionDTO(DeliveryOptionDTO deliveryOptionDTO) {
        this.deliveryOptionDTO = deliveryOptionDTO;
    }
}
