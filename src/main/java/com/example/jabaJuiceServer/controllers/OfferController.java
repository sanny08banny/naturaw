package com.example.jabaJuiceServer.controllers;
import com.example.jabaJuiceServer.adminDTOs.Offer;
import com.example.jabaJuiceServer.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sanny/offers")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    // Create a new offer
    @PostMapping("/new")
    public ResponseEntity<?> addOffer(@RequestPart("offer") Offer offer,
                                        @RequestPart("file") MultipartFile file,
                                        @RequestParam("email") String email) {
        offerService.saveOffer(offer, file,email);
        return ResponseEntity.ok("Offer saved successfully.");
    }

    // Update an existing offer
    @PutMapping("/{id}")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer updatedOffer) {
        Offer updated = offerService.updateOffer(id, updatedOffer);
        return ResponseEntity.ok(updated);
    }

    // Delete an offer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }

    // Get all offers
    @GetMapping
    public ResponseEntity<List<Offer>> getAllOffers() {
        List<Offer> offers = offerService.getAllOffers();
        return ResponseEntity.ok(offers);
    }

    // Get the latest offers
    @GetMapping("/latest")
    public Page<Offer> getLatestOffers() {
        Page<Offer> latestOffers = offerService.getLatestOffers();
        return latestOffers;
    }
}

