package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.entities.Comment;
import com.example.jabaJuiceServer.entities.LikedItem;
import com.example.jabaJuiceServer.entities.StoriYaJabaItem;
import com.example.jabaJuiceServer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<LikedItem, Long> {
    boolean existsByUserAndStoriYaJabaItem(User user, StoriYaJabaItem storiYaJabaItem);

    boolean existsByUserAndComment(User user, Comment comment);

    List<LikedItem> findLikedItemByStoriYaJabaItem(StoriYaJabaItem storiYaJabaItem);

    List<LikedItem> findLikedItemByComment(Comment comment);

    LikedItem findByUserAndStoriYaJabaItem(User user, StoriYaJabaItem storiYaJabaItem);

    boolean existsByUserAndProduct(User user, Product product);

    LikedItem findByUserAndProduct(User user, Product product);
}
