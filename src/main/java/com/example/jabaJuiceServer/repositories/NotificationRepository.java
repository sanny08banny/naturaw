package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
