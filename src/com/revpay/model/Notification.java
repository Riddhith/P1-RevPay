package com.revpay.model;

import java.sql.Timestamp;

public class Notification {
    private int id;
    private int userId;
    private String message;
    private boolean isRead;
    private Timestamp createdAt;

    public Notification(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Notification(int id, int userId, String message, boolean isRead, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public boolean isRead() { return isRead; }
    public Timestamp getCreatedAt() { return createdAt; }
}
