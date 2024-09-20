package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.adminDTOs.DeliveryOption;
import com.example.jabaJuiceServer.repositories.AdminUserRepository;
import com.example.jabaJuiceServer.repositories.DeliveryOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryOptionService {
    private AdminUserRepository adminUserRepository;
    private DeliveryOptionRepository deliveryOptionRepository;
    @Autowired
    public DeliveryOptionService(AdminUserRepository adminUserRepository, DeliveryOptionRepository deliveryOptionRepository) {
        this.adminUserRepository = adminUserRepository;
        this.deliveryOptionRepository = deliveryOptionRepository;
    }

    public DeliveryOption createDeliveryOption(DeliveryOption deliveryOption ,String email) {
        AdminUser adminUser = adminUserRepository.findByEmail(email);
        deliveryOption.setAdminUser(adminUser);
        return deliveryOptionRepository.save(deliveryOption);
    }

    public List<DeliveryOption> getAllDeliveryOptions() {
        return deliveryOptionRepository.findAll();
    }

    public DeliveryOption getDeliveryOptionById(Long id) {
        return deliveryOptionRepository.findById(id).orElse(null);
    }

    public List<DeliveryOption> getDeliveryOptionsByType(String deliveryType) {
        return deliveryOptionRepository.findByDeliveryType(deliveryType);
    }

    public void deleteDeliveryOption(Long id) {
        deliveryOptionRepository.deleteById(id);
    }

    public DeliveryOption updateDeliveryOption(Long id, DeliveryOption updatedDeliveryOption) {
        DeliveryOption existingDeliveryOption = deliveryOptionRepository.findById(id).orElse(null);
        if (existingDeliveryOption != null) {
            existingDeliveryOption.setDeliveryType(updatedDeliveryOption.getDeliveryType());
            existingDeliveryOption.setTimeAvailable(updatedDeliveryOption.getTimeAvailable());
            existingDeliveryOption.setAvailability(updatedDeliveryOption.isAvailability());
            existingDeliveryOption.setLocationsAvailable(updatedDeliveryOption.getLocationsAvailable());
            existingDeliveryOption.setCharges(updatedDeliveryOption.getCharges());
            return deliveryOptionRepository.save(existingDeliveryOption);
        }
        return null;
    }
}

