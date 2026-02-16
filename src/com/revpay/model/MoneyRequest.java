package com.revpay.model;

import java.sql.Timestamp;

public class MoneyRequest {
    private int requestId;
    private int requesterId;
    private int receiverId;
    private double amount;
    private String status;
    private Timestamp createdAt;

    public MoneyRequest(int requestId, int requesterId, int receiverId,
                        double amount, String status, Timestamp createdAt) {
        this.requestId = requestId;
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public MoneyRequest(int requesterId, int receiverId, double amount) {
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public int getRequestId() { return requestId; }
    public int getRequesterId() { return requesterId; }
    public int getReceiverId() { return receiverId; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }
}
