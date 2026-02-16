package com.revpay.repository;

import com.revpay.model.Notification;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationRepositoryTest {

    private static NotificationRepository notificationRepository;
    private static int testUserId;

    @BeforeAll
    static void setup() {
        notificationRepository = new NotificationRepository();

        // ⚠️ Use an existing user ID from your database
        // Change this if needed
        testUserId = 1;
    }

    @Test
    void testSaveNotification() {
        Notification notification = new Notification(
                testUserId,
                "JUnit test notification"
        );

        assertDoesNotThrow(() -> notificationRepository.save(notification),
                "Saving notification should not throw exception");
    }

    @Test
    void testGetUserNotifications() {
        List<Notification> notifications =
                notificationRepository.getUserNotifications(testUserId);

        assertNotNull(notifications, "Notification list should not be null");
        assertTrue(notifications.size() > 0,
                "User should have at least one notification");

        Notification n = notifications.get(0);

        assertEquals(testUserId, n.getUserId(), "User ID should match");
        assertNotNull(n.getMessage(), "Message should not be null");
        assertNotNull(n.getCreatedAt(), "CreatedAt should not be null");
    }
}
