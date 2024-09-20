package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.entities.Follow;
import com.example.jabaJuiceServer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByUserId(Long userId);
    boolean existsByUserAndFollowedUser(User user, User followedUser);

    List<Follow> findFollowersByFollowedUser(User followedUser);

//    List<User> findFollowsByUser(User user);

    Optional<Follow> findByUserAndFollowedUser(User user, User followedUser);

    List<Follow> findFollowsByUser(User user);
}
