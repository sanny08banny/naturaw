package com.example.jabaJuiceServer.services;

import com.example.jabaJuiceServer.entities.Transaction;
import com.example.jabaJuiceServer.entities.User;
import com.example.jabaJuiceServer.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    public void deleteTransactionById(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    public Transaction getTransactionByTransactionId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId).orElse(null);
    }

    public Page<Transaction> getRandomPageOfTransactions(Pageable pageable) {
        long totalTransactions = transactionRepository.count();
        int randomPage = (int) (Math.random() * (totalTransactions / pageable.getPageSize()));
        return transactionRepository.findAll(pageable.withPage(randomPage));
    }
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Method to get transactions for a user
    public List<Transaction> getTransactionsForUser(User user) {
        return transactionRepository.findByUser(user);
    }
    // Add other business logic or CRUD methods here if needed.
}
