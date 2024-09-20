package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    AdminUser findByEmail(String email);
}
