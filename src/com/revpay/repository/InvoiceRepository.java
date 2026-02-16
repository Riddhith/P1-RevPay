package com.revpay.repository;

import com.revpay.util.DBConnection;
import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InvoiceRepository {

    private static final Logger logger =
            LogManager.getLogger(InvoiceRepository.class);

    // Create invoice for customer
    public void createInvoice(int businessUserId, int customerId, double amount) {

        String sql = "INSERT INTO invoices (business_user_id, customer_id, amount) VALUES (?, ?, ?)";

        logger.info("Creating invoice for businessUserId {}", businessUserId);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, businessUserId);
            ps.setInt(2, customerId);
            ps.setDouble(3, amount);
            ps.executeUpdate();

            logger.info("Invoice created successfully");

        } catch (SQLException e) {
            logger.error("Invoice creation failed", e);
        }
    }

    // Mark invoice as PAID
    public boolean markInvoicePaid(int invoiceId) {

        String sql = "UPDATE invoices SET status = 'PAID' " +
                "WHERE invoice_id = ? AND status = 'UNPAID'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, invoiceId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Failed to update invoice status", e);
            return false;
        }
    }

    public ResultSet getInvoicesByBusinessUser(int businessUserId) {

        String sql = "SELECT * FROM invoices WHERE business_user_id = ?";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, businessUserId);
            return ps.executeQuery();
        } catch (SQLException e) {
            logger.error("Failed to fetch invoices", e);
            return null;
        }
    }


}
