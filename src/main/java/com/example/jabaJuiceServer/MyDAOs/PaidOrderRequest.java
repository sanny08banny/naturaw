package com.example.jabaJuiceServer.MyDAOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public class PaidOrderRequest {
    private double amount;
    private String phoneNumber,shortCode,token,payLinkUI;
    private String email,deliveryOptionId,selectedLocation;
    private List<OrderProductMap> orderProductMaps;

    public PaidOrderRequest() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<OrderProductMap> getOrderProductMaps() {
        return orderProductMaps;
    }

    public void setOrderProductMaps(List<OrderProductMap> orderProductMaps) {
        this.orderProductMaps = orderProductMaps;
    }

    public String getPayLinkUI() {
        return payLinkUI;
    }

    public void setPayLinkUI(String payLinkUI) {
        this.payLinkUI = payLinkUI;
    }

    public String getDeliveryOptionId() {
        return deliveryOptionId;
    }

    public void setDeliveryOptionId(String deliveryOptionId) {
        this.deliveryOptionId = deliveryOptionId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String selectedLocation) {
        this.selectedLocation = selectedLocation;
    }
}
