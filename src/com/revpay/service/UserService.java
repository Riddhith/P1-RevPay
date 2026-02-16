package com.revpay.service;

import com.revpay.model.User;
import com.revpay.repository.UserRepository;
import com.revpay.repository.WalletRepository;
import com.revpay.util.PasswordUtil;

public class UserService {

    private UserRepository repo = new UserRepository();
    private WalletRepository walletRepo = new WalletRepository();

    public boolean register(String name, String email, String phone,
                            String password, String pin, String type) {

        String hashedPassword = PasswordUtil.hash(password);
        String hashedPin = PasswordUtil.hash(pin);

        User user = new User(name, email, phone, hashedPassword, hashedPin, type);
        boolean isSaved = repo.save(user);

        if (isSaved) {
            // Create wallet after successful registration
            int userId = repo.getUserIdByEmailOrPhone(email);
            walletRepo.createWallet(userId);
            return true;
        }
        return false;
    }

    public boolean login(String email, String password) {
        String hashedPassword = PasswordUtil.hash(password);
        return repo.validateLogin(email, hashedPassword);
    }

    public int getUserId(String email) {
        return repo.getUserIdByEmailOrPhone(email);
    }

    // ✅ NEW METHOD (IMPORTANT)
    public String getAccountType(String email) {
        return repo.getAccountTypeByEmail(email);
    }
}

