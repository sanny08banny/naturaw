package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.adminDTOs.PayLink;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.repositories.AdminUserRepository;
import com.example.jabaJuiceServer.repositories.PayLinkRepository;
import com.example.jabaJuiceServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;

@Service
public class PayLinkService {
    private AdminUserRepository adminUserRepository;
    private PayLinkRepository paymentMethodRepository;
    private UserRepository userRepository;

    @Autowired
    public PayLinkService(AdminUserRepository adminUserRepository, PayLinkRepository paymentMethodRepository, UserRepository userRepository) {
        this.adminUserRepository = adminUserRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.userRepository = userRepository;
    }

    public PayLink createPaymentMethod(PayLink payLink, String email) {
        AdminUser adminUser = adminUserRepository.findByEmail(email);
        payLink.setAdminUser(adminUser);
        return paymentMethodRepository.save(payLink);
    }

    public PayLink updatePaymentMethod(Long paymentMethodId, PayLink updatedPaymentMethod) {
        PayLink existingPaymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new EntityNotFoundException("PayLink not found"));

        existingPaymentMethod.setTransactionType(updatedPaymentMethod.getTransactionType());
        existingPaymentMethod.setRecipientNumber(updatedPaymentMethod.getRecipientNumber());
        existingPaymentMethod.setShortcode(updatedPaymentMethod.getShortcode());
        existingPaymentMethod.setPaybillNumber(updatedPaymentMethod.getPaybillNumber());
        existingPaymentMethod.setAccountNumber(updatedPaymentMethod.getAccountNumber());
        existingPaymentMethod.setPochiRecipientNumber(updatedPaymentMethod.getPochiRecipientNumber());

        return paymentMethodRepository.save(existingPaymentMethod);
    }

    public void deletePaymentMethod(Long paymentMethodId) {
        PayLink paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new EntityNotFoundException("PayLink not found"));

        paymentMethodRepository.delete(paymentMethod);
    }

    public List<PayLink> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    public Page<PayLink> getPaymentMethodsPaged(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return paymentMethodRepository.findAll(pageable);
    }

    public PayLink getPaymentMethodById(Long paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new EntityNotFoundException("PayLink not found"));
    }

    public List<PayLink> getPaymentMethodsByUser(String email) {
        //remember to set this method to get palinks for one email only
        AdminUser adminUser = adminUserRepository.findByEmail(email);
        return paymentMethodRepository.findAll();
    }
    public List<PayLink> getPaymentMethods(String email) {
        //remember to set this method to get palinks for one email only
        AdminUser adminUser = adminUserRepository.findByEmail(email);
        return paymentMethodRepository.findByAdminUser(adminUser);
    }
}

