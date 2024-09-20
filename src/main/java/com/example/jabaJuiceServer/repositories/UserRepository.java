package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFullName(String username);

    User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(u.userName) LIKE :query " +
            "OR LOWER(u.fullName) LIKE :query " +
            "OR LOWER(u.email) LIKE :query " +
            "OR LOWER(u.role) LIKE :query " +
            "OR LOWER(u.gender) LIKE :query " +
            "OR LOWER(u.address) LIKE :query " +
            "OR LOWER(u.accountType) LIKE :query " +
            "OR CAST(u.age AS string) LIKE :query")
    List<User> searchUsersByQuery(String query);

    User findByUserName(String newUsername);
}
