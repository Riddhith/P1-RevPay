package com.revpay.repository;

import com.revpay.util.DBConnection;
import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WalletRepository {

    private static final Logger logger =
            LogManager.getLogger(WalletRepository.class);

    // Create wallet using PL/SQL
    public void createWallet(int userId) {
        String sql = "{ CALL create_wallet(?) }";

        logger.info("Creating wallet for userId: {}", userId);

        try (Connection con = DBConnection.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, userId);
            cs.execute();

            logger.info("Wallet created successfully for userId: {}", userId);

        } catch (SQLException e) {
            logger.error("Error while creating wallet for userId: {}", userId, e);
        }
    }

    // Get wallet balance using PL/SQL
    public double getBalance(int userId) {
        String sql = "{ CALL get_wallet_balance(?, ?) }";

        logger.debug("Fetching wallet balance for userId: {}", userId);

        try (Connection con = DBConnection.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, userId);
            cs.registerOutParameter(2, Types.DOUBLE);

            cs.execute();

            double balance = cs.getDouble(2);
            logger.info("Balance for userId {} is {}", userId, balance);
            return balance;

        } catch (SQLException e) {
            logger.error("Error fetching wallet balance for userId: {}", userId, e);
        }
        return 0;
    }

    // Update wallet balance using PL/SQL
    public boolean updateBalance(int userId, double newBalance) {
        String sql = "{ CALL update_wallet_balance(?, ?, ?) }";

        logger.info("Updating wallet balance for userId: {} to {}", userId, newBalance);

        try (Connection con = DBConnection.getConnection();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, userId);
            cs.setDouble(2, newBalance);
            cs.registerOutParameter(3, Types.INTEGER);

            cs.execute();

            int rowsUpdated = cs.getInt(3);

            if (rowsUpdated > 0) {
                logger.info("Wallet balance updated successfully for userId: {}", userId);
                return true;
            } else {
                logger.warn("Wallet balance update failed for userId: {}", userId);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error updating wallet balance for userId: {}", userId, e);
            return false;
        }
    }
}
