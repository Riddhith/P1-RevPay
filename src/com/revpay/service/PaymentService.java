package com.revpay.service;

import com.revpay.model.Transaction;
import com.revpay.repository.TransactionRepository;
import com.revpay.repository.UserRepository;
import com.revpay.repository.WalletRepository;

public class PaymentService {

    private WalletRepository walletRepo = new WalletRepository();
    private UserRepository userRepo = new UserRepository();
    private TransactionRepository txnRepo = new TransactionRepository();

    public boolean sendMoney(int senderId, String receiverInput, double amount) {

        int receiverId = userRepo.getUserIdByEmailOrPhone(receiverInput);
        if (receiverId == -1) {
            System.out.println("Receiver not found!");
            return false;
        }

        double senderBalance = walletRepo.getBalance(senderId);
        if (senderBalance < amount) {
            System.out.println("Insufficient balance!");
            return false;
        }

        // Update balances
        walletRepo.updateBalance(senderId, senderBalance - amount);

        double receiverBalance = walletRepo.getBalance(receiverId);
        walletRepo.updateBalance(receiverId, receiverBalance + amount);

        // Save ONLY ONE transaction
        Transaction txn = new Transaction(senderId, receiverId, amount, "TRANSFER", "SUCCESS");
        txnRepo.save(txn);

        return true;
    }

}
