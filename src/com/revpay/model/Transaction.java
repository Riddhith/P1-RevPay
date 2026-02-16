package com.revpay.model;

import java.sql.Timestamp;

public class Transaction {
    private int txnId;
    private int senderId;
    private int receiverId;
    private double amount;
    private String type;
    private String status;
    private Timestamp createdAt;

    // Constructor for FETCHING from DB
    public Transaction(int txnId, int senderId, int receiverId, double amount,
                       String type, String status, Timestamp createdAt) {
        this.txnId = txnId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Constructor for SAVING new transactions
    public Transaction(int senderId, int receiverId, double amount,
                       String type, String status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.type = type;
        this.status = status;
    }

    public int getTxnId() { return txnId; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }
}
