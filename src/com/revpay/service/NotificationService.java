package com.revpay.service;

import com.revpay.model.Notification;
import com.revpay.repository.NotificationRepository;
import java.util.List;

public class NotificationService {

    private NotificationRepository repo = new NotificationRepository();

    public void createNotification(int userId, String message) {
        repo.save(new Notification(userId, message));
    }

    public List<Notification> getNotifications(int userId) {
        return repo.getUserNotifications(userId);
    }
}
