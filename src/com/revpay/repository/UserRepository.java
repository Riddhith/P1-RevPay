package com.revpay.repository;

import com.revpay.model.User;
import com.revpay.util.DBConnection;

import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserRepository {

    private static final Logger logger =
            LogManager.getLogger(UserRepository.class);

    // Save new user
    public boolean save(User user) {
        String sql = "INSERT INTO users(full_name, email, phone, password_hash, transaction_pin, account_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        logger.info("Attempting to save new user with email: {}", user.getEmail());

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getTransactionPin());
            ps.setString(6, user.getAccountType());

            boolean success = ps.executeUpdate() > 0;

            if (success) {
                logger.info("User saved successfully: {}", user.getEmail());
            } else {
                logger.warn("User save failed for email: {}", user.getEmail());
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error while saving user with email: {}", user.getEmail(), e);
            return false;
        }
    }

    // Validate login
    public boolean validateLogin(String email, String passwordHash) {
        String sql = "SELECT 1 FROM users WHERE email = ? AND password_hash = ?";

        logger.info("Validating login for email: {}", email);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, passwordHash);

            ResultSet rs = ps.executeQuery();
            boolean valid = rs.next();

            if (valid) {
                logger.info("Login successful for email: {}", email);
            } else {
                logger.warn("Invalid login attempt for email: {}", email);
            }

            return valid;

        } catch (SQLException e) {
            logger.error("Login validation failed for email: {}", email, e);
            return false;
        }
    }

    // Get user ID using email or phone
    public int getUserIdByEmailOrPhone(String input) {
        String sql = "SELECT user_id FROM users WHERE email = ? OR phone = ?";

        logger.debug("Fetching user ID using input: {}", input);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, input);
            ps.setString(2, input);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                logger.info("User ID {} found for input: {}", userId, input);
                return userId;
            } else {
                logger.warn("No user found for input: {}", input);
            }

        } catch (SQLException e) {
            logger.error("Error fetching user ID for input: {}", input, e);
        }
        return -1;
    }
    // Get account type by email
    public String getAccountTypeByEmail(String email) {
        String sql = "SELECT account_type FROM users WHERE email = ?";

        logger.debug("Fetching account type for email: {}", email);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String type = rs.getString("account_type");
                logger.info("Account type for {} is {}", email, type);
                return type;
            } else {
                logger.warn("No account type found for email: {}", email);
            }

        } catch (SQLException e) {
            logger.error("Error fetching account type for email: {}", email, e);
        }
        return null;
    }

}
