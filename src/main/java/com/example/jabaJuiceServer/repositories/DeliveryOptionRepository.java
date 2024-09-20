package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.adminDTOs.DeliveryOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryOptionRepository extends JpaRepository<DeliveryOption, Long> {
    List<DeliveryOption> findByDeliveryType(String deliveryType);
}
