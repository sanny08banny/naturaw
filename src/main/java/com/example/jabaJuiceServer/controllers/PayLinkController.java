package com.example.jabaJuiceServer.controllers;

import com.example.jabaJuiceServer.adminDTOs.PayLink;
import com.example.jabaJuiceServer.services.PayLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sanny/pay-links")
public class PayLinkController {
    private PayLinkService payLinkService;

    @Autowired
    public PayLinkController(PayLinkService payLinkService) {
        this.payLinkService = payLinkService;
    }

    @PostMapping("/new")
    public ResponseEntity<PayLink> createPaymentMethod(@RequestBody PayLink paymentMethod,
                                                       @RequestParam("email") String email) {
        PayLink createdPaymentMethod = payLinkService.createPaymentMethod(paymentMethod, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPaymentMethod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayLink> updatePaymentMethod(@PathVariable Long id,
                                                       @RequestBody PayLink updatedPaymentMethod) {
        PayLink updatedMethod = payLinkService.updatePaymentMethod(id, updatedPaymentMethod);
        return ResponseEntity.ok(updatedMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        payLinkService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<PayLink>> getAllPaymentMethods() {
        List<PayLink> paymentMethods = payLinkService.getAllPaymentMethods();
        return ResponseEntity.ok(paymentMethods);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<PayLink>> getPaymentMethodsPaged(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int pageSize) {
        Page<PayLink> paymentMethodPage = payLinkService.getPaymentMethodsPaged(page, pageSize);
        return ResponseEntity.ok(paymentMethodPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayLink> getPaymentMethodById(@PathVariable Long id) {
        PayLink paymentMethod = payLinkService.getPaymentMethodById(id);
        return ResponseEntity.ok(paymentMethod);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PayLink>> getPaymentMethodsByUser(@RequestParam String email) {
        List<PayLink> paymentMethods = payLinkService.getPaymentMethodsByUser(email);
        return ResponseEntity.ok(paymentMethods);
    }
}

