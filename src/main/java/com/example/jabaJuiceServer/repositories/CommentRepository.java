package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
