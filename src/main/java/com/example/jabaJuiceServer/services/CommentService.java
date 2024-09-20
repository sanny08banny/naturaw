package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.adminDTOs.Product;
import com.example.jabaJuiceServer.entities.Comment;
import com.example.jabaJuiceServer.entities.StoriYaJabaItem;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.repositories.CommentRepository;
import com.example.jabaJuiceServer.repositories.ProductRepository;
import com.example.jabaJuiceServer.repositories.StoriYaJabaRepository;
import com.example.jabaJuiceServer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final StoriYaJabaRepository storiYaJabaRepository;
    private ProductRepository productRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, StoriYaJabaRepository storiYaJabaRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.storiYaJabaRepository = storiYaJabaRepository;
        this.productRepository = productRepository;
    }

    public Comment createStoriComment(String text, Long fileName, String email) {
        User user = userRepository.findByEmail(email);
        StoriYaJabaItem storiYaJabaItem = storiYaJabaRepository.findStoriById(fileName);
        Comment comment = new Comment();
        comment.setStoriYaJabaItem(storiYaJabaItem);
        comment.setUser(user);
        comment.setText(text);
        return commentRepository.save(comment);
    }
    public Comment createProductComment(String text, Long id, String email) {
        User user = userRepository.findByEmail(email);
        Optional<Product> product = productRepository.findById(id);
        Comment comment = new Comment();
        comment.setProduct(product.get());
        comment.setUser(user);
        comment.setText(text);
        return commentRepository.save(comment);
    }


    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}

