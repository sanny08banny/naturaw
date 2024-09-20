package com.example.jabaJuiceServer.adminDTOs;

import com.example.jabaJuiceServer.entities.Order;
import com.example.jabaJuiceServer.entities.UserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class DeliveryOptionDTO {
    private Long id;

    private String deliveryType;
    private String timeAvailable;
    private boolean availability;
    private List<String> locationsAvailable;
    private double charges;
    private UserDTO userDTO;
    // Constructors, getters, and setters

    // Default constructor (empty constructor required by JPA)
    public DeliveryOptionDTO() {
    }

    // Parameterized constructor

    // Getters and setters for all fields

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

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    // Additional methods (if needed)
    // ...
}

