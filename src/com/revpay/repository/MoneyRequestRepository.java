package com.revpay.repository;

import com.revpay.model.MoneyRequest;
import com.revpay.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoneyRequestRepository {

    private static final Logger logger =
            LogManager.getLogger(MoneyRequestRepository.class);

    // Save a new money request
    public void save(MoneyRequest request) {
        String sql = "INSERT INTO money_requests (requester_id, receiver_id, amount) VALUES (?, ?, ?)";

        logger.info(
                "Creating money request | requesterId={} receiverId={} amount={}",
                request.getRequesterId(),
                request.getReceiverId(),
                request.getAmount()
        );

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, request.getRequesterId());
            ps.setInt(2, request.getReceiverId());
            ps.setDouble(3, request.getAmount());
            ps.executeUpdate();

            logger.info("Money request saved successfully");

        } catch (SQLException e) {
            logger.error(
                    "Error saving money request | requesterId={} receiverId={}",
                    request.getRequesterId(),
                    request.getReceiverId(),
                    e
            );
        }
    }

    // Get all pending requests for a receiver
    public List<MoneyRequest> getPendingRequests(int receiverId) {
        List<MoneyRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM money_requests WHERE receiver_id = ? AND status = 'PENDING'";

        logger.debug("Fetching pending money requests for receiverId: {}", receiverId);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, receiverId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new MoneyRequest(
                        rs.getInt("request_id"),
                        rs.getInt("requester_id"),
                        rs.getInt("receiver_id"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }

            logger.info(
                    "Fetched {} pending money requests for receiverId {}",
                    list.size(),
                    receiverId
            );

        } catch (SQLException e) {
            logger.error(
                    "Error fetching pending requests for receiverId: {}",
                    receiverId,
                    e
            );
        }
        return list;
    }

    // Update request status (ACCEPTED / REJECTED)
    public void updateStatus(int requestId, String status) {
        String sql = "UPDATE money_requests SET status = ? WHERE request_id = ?";

        logger.info(
                "Updating money request status | requestId={} status={}",
                requestId,
                status
        );

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, requestId);
            ps.executeUpdate();

            logger.info("Money request status updated successfully");

        } catch (SQLException e) {
            logger.error(
                    "Error updating money request status | requestId={}",
                    requestId,
                    e
            );
        }
    }

    // Get money request by ID
    public MoneyRequest getById(int requestId) {
        String sql = "SELECT * FROM money_requests WHERE request_id = ?";

        logger.debug("Fetching money request by requestId: {}", requestId);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Money request found for requestId: {}", requestId);
                return new MoneyRequest(
                        rs.getInt("request_id"),
                        rs.getInt("requester_id"),
                        rs.getInt("receiver_id"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                );
            } else {
                logger.warn("No money request found for requestId: {}", requestId);
            }

        } catch (SQLException e) {
            logger.error(
                    "Error fetching money request for requestId: {}",
                    requestId,
                    e
            );
        }
        return null;
    }
}
