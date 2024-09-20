package com.example.jabaJuiceServer.controllers;

import com.example.jabaJuiceServer.adminDTOs.DeliveryOption;
import com.example.jabaJuiceServer.services.DeliveryOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sanny/delivery-options")
public class DeliveryOptionController {

    @Autowired
    private DeliveryOptionService deliveryOptionService;

    @PostMapping
    public DeliveryOption createDeliveryOption(@RequestBody DeliveryOption deliveryOption, @RequestParam("email") String email) {
        return deliveryOptionService.createDeliveryOption(deliveryOption,email);
    }

    @GetMapping
    public List<DeliveryOption> getAllDeliveryOptions() {
        return deliveryOptionService.getAllDeliveryOptions();
    }

    @GetMapping("/{id}")
    public DeliveryOption getDeliveryOptionById(@PathVariable Long id) {
        return deliveryOptionService.getDeliveryOptionById(id);
    }

    @GetMapping("/type/{deliveryType}")
    public List<DeliveryOption> getDeliveryOptionsByType(@PathVariable String deliveryType) {
        return deliveryOptionService.getDeliveryOptionsByType(deliveryType);
    }

    @PutMapping("/{id}")
    public DeliveryOption updateDeliveryOption(@PathVariable Long id, @RequestBody DeliveryOption updatedDeliveryOption) {
        return deliveryOptionService.updateDeliveryOption(id, updatedDeliveryOption);
    }

    @DeleteMapping("/{id}")
    public void deleteDeliveryOption(@PathVariable Long id) {
        deliveryOptionService.deleteDeliveryOption(id);
    }
}

