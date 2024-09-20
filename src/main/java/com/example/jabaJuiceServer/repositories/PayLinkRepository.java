package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.adminDTOs.PayLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayLinkRepository extends JpaRepository<PayLink, Long> {
    List<PayLink> findByAdminUser(AdminUser adminUser);

    PayLink findByUI(String pay_link_u);
}
