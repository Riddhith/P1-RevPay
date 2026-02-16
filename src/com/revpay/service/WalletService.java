package com.revpay.service;

import com.revpay.repository.WalletRepository;

public class WalletService {

    private WalletRepository repo = new WalletRepository();

    public double checkBalance(int userId) {
        return repo.getBalance(userId);
    }

    public boolean addMoney(int userId, double amount) {
        double current = repo.getBalance(userId);
        return repo.updateBalance(userId, current + amount);
    }

    public boolean withdrawMoney(int userId, double amount) {
        double current = repo.getBalance(userId);
        if (current >= amount) {
            return repo.updateBalance(userId, current - amount);
        }
        return false;
    }
}
