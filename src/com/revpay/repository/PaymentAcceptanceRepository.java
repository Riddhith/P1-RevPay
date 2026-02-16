package com.revpay.repository;

import com.revpay.util.DBConnection;
import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaymentAcceptanceRepository {

    private static final Logger logger =
            LogManager.getLogger(PaymentAcceptanceRepository.class);

    // Accept payment from customer
    public void acceptPayment(int businessUserId, int payerId, double amount) {

        String sql = "INSERT INTO business_payments (business_user_id, payer_id, amount) VALUES (?, ?, ?)";

        logger.info("Accepting payment for businessUserId {}", businessUserId);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, businessUserId);
            ps.setInt(2, payerId);
            ps.setDouble(3, amount);
            ps.executeUpdate();

            logger.info("Payment accepted successfully");

        } catch (SQLException e) {
            logger.error("Payment acceptance failed", e);
        }
    }
}
