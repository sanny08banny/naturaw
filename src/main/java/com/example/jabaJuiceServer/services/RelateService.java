package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.entities.*;
import com.example.jabaJuiceServer.repositories.RelatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelateService {
    private RelatesRepository relatesRepository;
    private NotificationService notificationService;

    @Autowired
    public RelateService(RelatesRepository relatesRepository, NotificationService notificationService) {
        this.relatesRepository = relatesRepository;
        this.notificationService = notificationService;
    }

    public void relateStori(User user, StoriYaJabaItem storiYaJabaItem) {
        RelatedItem relatedItem = new RelatedItem();
        relatedItem.setUser(user);
        relatedItem.setStoriYaJabaItem(storiYaJabaItem);

        notificationService.sendRelateNotification(storiYaJabaItem.getUserDTO().getId(),user);

        relatesRepository.save(relatedItem);
    }
    public void removeRelateStori(User user, StoriYaJabaItem storiYaJabaItem) {
        RelatedItem relatedItem = relatesRepository.findByUserAndStoriYaJabaItem(user,storiYaJabaItem);

        user.getRelatedItems().remove(relatedItem);
        relatesRepository.delete(relatedItem);
    }

    public boolean isRelatedToStori(User user, StoriYaJabaItem storiYaJabaItem) {
        return relatesRepository.existsByUserAndStoriYaJabaItem(user, storiYaJabaItem);
    }
    public void relateProducts(User user, Product product) {
        RelatedItem relatedItem = new RelatedItem();
        relatedItem.setUser(user);
        relatedItem.setProduct(product);
        relatesRepository.save(relatedItem);
    }
    public void removeRelateProduct(User user, Product product) {
        RelatedItem relatedItem = relatesRepository.findByUserAndProduct(user,product);

        user.getRelatedItems().remove(relatedItem);
        relatesRepository.delete(relatedItem);
    }
    public boolean isRelatedToProduct(User user, Product product) {
        return relatesRepository.existsByUserAndProduct(user, product);
    }

    public void relateComment(User user, Comment comment) {
        RelatedItem relatedItem = new RelatedItem();
        relatedItem.setUser(user);
        relatedItem.setComment(comment);
        relatesRepository.save(relatedItem);
    }

    public boolean isRelatedToComment(User user, Comment comment) {
        return relatesRepository.existsByUserAndComment(user, comment);
    }

    public void unRelate(RelatedItem relatedItem) {
        relatesRepository.delete(relatedItem);
    }

    public int getRelateForStori(StoriYaJabaItem storiYaJabaItem) {
        List<RelatedItem> relatedItems = relatesRepository.findRelatedItemByStoriYaJabaItem(storiYaJabaItem);
        if (relatedItems != null) {
            return relatedItems.size();
        }
        return 0;
    }

    public int getRelateForComment(Comment comment) {
        List<RelatedItem> relatedItems = relatesRepository.findRelatedItemByComment(comment);
        if (relatedItems != null) {
            return relatedItems.size();
        }
        return 0;
    }
}
