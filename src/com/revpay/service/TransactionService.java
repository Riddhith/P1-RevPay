package com.revpay.service;

import com.revpay.model.Transaction;
import com.revpay.repository.TransactionRepository;
import java.util.List;

public class TransactionService {

    private TransactionRepository repo = new TransactionRepository();

    public List<Transaction> getUserTransactions(int userId) {
        return repo.getTransactionsByUser(userId);
    }
    public void saveTransaction(int senderId, int receiverId, double amount) {
        Transaction txn = new Transaction(senderId, receiverId, amount, "TRANSFER", "SUCCESS");
        repo.save(txn);
    }
}
