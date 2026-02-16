package com.revpay.repository;

import com.revpay.model.Transaction;
import com.revpay.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransactionRepository {

    private static final Logger logger =
            LogManager.getLogger(TransactionRepository.class);

    // Save a transaction
    public void save(Transaction txn) {
        String sql = "INSERT INTO transactions " +
                "(sender_id, receiver_id, amount, txn_type, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        logger.info(
                "Saving transaction | senderId={} receiverId={} amount={} type={}",
                txn.getSenderId(),
                txn.getReceiverId(),
                txn.getAmount(),
                txn.getType()
        );

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, txn.getSenderId());
            ps.setInt(2, txn.getReceiverId());
            ps.setDouble(3, txn.getAmount());
            ps.setString(4, txn.getType());
            ps.setString(5, txn.getStatus());

            ps.executeUpdate();

            logger.info("Transaction saved successfully");

        } catch (SQLException e) {
            logger.error(
                    "Error saving transaction | senderId={} receiverId={}",
                    txn.getSenderId(),
                    txn.getReceiverId(),
                    e
            );
        }
    }

    // Fetch all transactions of a user
    public List<Transaction> getTransactionsByUser(int userId) {
        List<Transaction> list = new ArrayList<>();

        String sql =
                "SELECT * FROM transactions " +
                        "WHERE sender_id = ? OR receiver_id = ? " +
                        "ORDER BY created_at DESC";

        logger.debug("Fetching transaction history for userId: {}", userId);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction txn = new Transaction(
                        rs.getInt("txn_id"),
                        rs.getInt("sender_id"),
                        rs.getInt("receiver_id"),
                        rs.getDouble("amount"),
                        rs.getString("txn_type"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                );
                list.add(txn);
            }

            logger.info(
                    "Fetched {} transactions for userId {}",
                    list.size(),
                    userId
            );

        } catch (SQLException e) {
            logger.error(
                    "Error fetching transaction history for userId: {}",
                    userId,
                    e
            );
        }
        return list;
    }
}
