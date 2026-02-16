package com.revpay.model;

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String phone;
    private String passwordHash;
    private String transactionPin;
    private String accountType;

    public User(String fullName, String email, String phone, String passwordHash,
                String transactionPin, String accountType) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.transactionPin = transactionPin;
        this.accountType = accountType;
    }

    // Getters

    public int getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPasswordHash() { return passwordHash; }
    public String getTransactionPin() { return transactionPin; }
    public String getAccountType() { return accountType; }
}

