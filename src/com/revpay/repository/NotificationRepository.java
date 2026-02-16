package com.revpay.repository;

import com.revpay.model.Notification;
import com.revpay.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationRepository {

    private static final Logger LOGGER = Logger.getLogger(NotificationRepository.class.getName());

    // Save a notification
    public void save(Notification n) {
        String sql = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, n.getUserId());
            ps.setString(2, n.getMessage());
            ps.executeUpdate();

            LOGGER.info("Notification saved for userId=" + n.getUserId());

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Error saving notification for userId=" + n.getUserId(), e);
        }
    }

    // Get notifications for a user
    public List<Notification> getUserNotifications(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                boolean isRead = rs.getInt("is_read") == 1;

                list.add(new Notification(
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        isRead,
                        rs.getTimestamp("created_at")
                ));
            }

            LOGGER.info("Fetched notifications for userId=" + userId +
                    ", count=" + list.size());

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Error fetching notifications for userId=" + userId, e);
        }
        return list;
    }
}
