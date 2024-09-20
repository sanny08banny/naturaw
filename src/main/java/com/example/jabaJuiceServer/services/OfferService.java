package com.example.jabaJuiceServer.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.jabaJuiceServer.adminDTOs.AdminUser;
import com.example.jabaJuiceServer.adminDTOs.Offer;
import com.example.jabaJuiceServer.config.StorageConfig;
import com.example.jabaJuiceServer.repositories.OfferRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferService {

    private static final String S3_BUCKET_NAME = "naturaw";
    private final OfferRepository offerRepository;
    private AmazonS3 s3Client;
    private StorageConfig storageConfig;
    private AdminUserService adminUserService;
    private ProductService productService;

    @Autowired
    public OfferService(OfferRepository offerRepository, StorageConfig storageConfig,
                        AdminUserService adminUserService, ProductService productService) {
        this.offerRepository = offerRepository;
        this.storageConfig = storageConfig;
        this.adminUserService = adminUserService;
        this.productService = productService;
        this.s3Client = storageConfig.s3Client();
    }

    // Method to create an offer
    public void saveOffer(Offer offer, MultipartFile image, String email) {
        AdminUser user = adminUserService.findByEmail(email);
        if (user != null) {
            // Save the image file to the file system
            String fileUrl = saveImageToFileSystem(image);
            // Create a new Product object
//            product.setAdminUser(user);
            offer.setFilePath(fileUrl);
            offer.setAdminUser(user);

            // Save the product in the repository
            offerRepository.save(offer);
        }
    }
    private String saveImageToFileSystem(MultipartFile file) {
        try {
            // Generate a unique file name
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "." + extension;

            // Upload the file to Amazon S3
            String s3FilePath = "offers/" + fileName;
            s3Client.putObject(new PutObjectRequest(S3_BUCKET_NAME, s3FilePath, file.getInputStream(), null));

            // Get the URL of the uploaded file from S3
            return s3Client.getUrl(S3_BUCKET_NAME, s3FilePath).toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save the image file.", e);
        }
    }
    // Method to delete an offer by ID
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }

    // Method to update an existing offer
    public Offer updateOffer(Long id, Offer updatedOffer) {
        Optional<Offer> existingOfferOptional = offerRepository.findById(id);

        if (existingOfferOptional.isPresent()) {
            Offer existingOffer = existingOfferOptional.get();
            // Update fields of the existing offer with fields from updatedOffer
            existingOffer.setFilePath(updatedOffer.getFilePath());
            existingOffer.setDescription(updatedOffer.getDescription());
            existingOffer.setDatePosted(updatedOffer.getDatePosted());
            existingOffer.setExpiryDate(updatedOffer.getExpiryDate());
            existingOffer.setProducts(updatedOffer.getProducts());
            existingOffer.setUserDTO(updatedOffer.getUserDTO());

            return offerRepository.save(existingOffer);
        } else {
            throw new IllegalArgumentException("Offer not found with ID: " + id);
        }
    }

    // Method to retrieve all offers
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    // Method to retrieve the latest offers (based on datePosted)
    // OfferService.java
    public Page<Offer> getLatestOffers() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Offer> latestOffers = offerRepository.findAll(pageRequest);
        return latestOffers;
    }

}

