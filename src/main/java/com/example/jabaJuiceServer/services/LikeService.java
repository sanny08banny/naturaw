package com.example.jabaJuiceServer.services;


import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.entities.*;
import com.example.jabaJuiceServer.repositories.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {
    private LikesRepository likesRepository;
    private NotificationService notificationService;

    @Autowired
    public LikeService(LikesRepository likesRepository, NotificationService notificationService) {
        this.likesRepository = likesRepository;
        this.notificationService = notificationService;
    }

    public void likeStori(User user, StoriYaJabaItem storiYaJabaItem) {
        LikedItem likedItem = new LikedItem();
        likedItem.setUser(user);
        likedItem.setStoriYaJabaItem(storiYaJabaItem);
        likesRepository.save(likedItem);
        notificationService.sendLikeNotification(storiYaJabaItem.getUserDTO().getId(),user);
    }
    public void removeLikeStori(User user, StoriYaJabaItem storiYaJabaItem) {
        LikedItem likedItem = likesRepository.findByUserAndStoriYaJabaItem(user,storiYaJabaItem);
        user.getLikedItems().remove(likedItem);
        likesRepository.delete(likedItem);
    }

    public boolean isLikedStori(User user, StoriYaJabaItem storiYaJabaItem) {
        return likesRepository.existsByUserAndStoriYaJabaItem(user, storiYaJabaItem);
    }
    public void likeProduct(User user, Product product) {
        LikedItem likedItem = new LikedItem();
        likedItem.setUser(user);
        likedItem.setProduct(product);
        likesRepository.save(likedItem);
    }
    public void removeLikeProduct(User user, Product product) {
        LikedItem likedItem = likesRepository.findByUserAndProduct(user,product);
        user.getLikedItems().remove(likedItem);
        likesRepository.delete(likedItem);
    }

    public boolean isLikedProduct(User user, Product product) {
        return likesRepository.existsByUserAndProduct(user, product);
    }

    public void likeComment(User user, Comment comment) {
        LikedItem likedItem = new LikedItem();
        likedItem.setUser(user);
        likedItem.setComment(comment);
        likesRepository.save(likedItem);
    }

    public boolean isLikedComment(User user, Comment comment) {
        return likesRepository.existsByUserAndComment(user, comment);
    }

    public void unLike(LikedItem likedItem) {
        likesRepository.delete(likedItem);
    }

    public int getLikesForStori(StoriYaJabaItem storiYaJabaItem) {
        List<LikedItem> likedItems = likesRepository.findLikedItemByStoriYaJabaItem(storiYaJabaItem);
        if (likedItems != null) {
            return likedItems.size();
        }
        return 0;
    }

    public int getLikesForComment(Comment comment) {
        List<LikedItem> likedItems = likesRepository.findLikedItemByComment(comment);
        if (likedItems != null) {
            return likedItems.size();
        }
        return 0;
    }
}
