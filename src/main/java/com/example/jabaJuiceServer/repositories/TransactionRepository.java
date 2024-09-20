package com.example.jabaJuiceServer.repositories;

import com.example.jabaJuiceServer.entities.Transaction;
import com.example.jabaJuiceServer.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);

    List<Transaction> findByUser(User user);
}
