package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.adminDTOs.DeliveryOption;
import com.example.jabaJuiceServer.entities.Order;
import com.example.jabaJuiceServer.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Page<Order> findByUser(User user, Pageable pageable);

    List<Order> findByDeliveryOption(DeliveryOption deliveryOption);
}
