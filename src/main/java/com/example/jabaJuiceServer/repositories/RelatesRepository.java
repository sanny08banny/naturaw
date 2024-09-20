package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.entities.Comment;
import com.example.jabaJuiceServer.entities.RelatedItem;
import com.example.jabaJuiceServer.entities.StoriYaJabaItem;
import com.example.jabaJuiceServer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelatesRepository extends JpaRepository<RelatedItem, Long> {
    boolean existsByUserAndStoriYaJabaItem(User user, StoriYaJabaItem storiYaJabaItem);

    boolean existsByUserAndComment(User user, Comment comment);

    List<RelatedItem> findRelatedItemByStoriYaJabaItem(StoriYaJabaItem storiYaJabaItem);

    List<RelatedItem> findRelatedItemByComment(Comment comment);

    RelatedItem findByUserAndStoriYaJabaItem(User user, StoriYaJabaItem storiYaJabaItem);

    boolean existsByUserAndProduct(User user, Product product);

    RelatedItem findByUserAndProduct(User user, Product product);
}
