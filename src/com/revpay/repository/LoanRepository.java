package com.revpay.repository;

import com.revpay.util.DBConnection;
import java.sql.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoanRepository {

    private static final Logger logger =
            LogManager.getLogger(LoanRepository.class);

    // Apply for loan (BUSINESS only)
    public void applyLoan(int businessUserId, double amount) {

        String sql = "INSERT INTO business_loans (business_user_id, amount) VALUES (?, ?)";

        logger.info("Loan application started for businessUserId {}", businessUserId);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, businessUserId);
            ps.setDouble(2, amount);
            ps.executeUpdate();

            logger.info("Loan applied successfully for businessUserId {}", businessUserId);

        } catch (SQLException e) {
            logger.error("Loan application failed", e);
        }
    }
    public ResultSet getLoansByBusinessUser(int businessUserId) {

        String sql = "SELECT * FROM business_loans WHERE business_user_id = ?";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, businessUserId);
            return ps.executeQuery();
        } catch (SQLException e) {
            logger.error("Failed to fetch loans", e);
            return null;
        }
    }

    public boolean updateLoanStatus(int loanId, String status) {

        String sql = "UPDATE business_loans SET status = ? WHERE loan_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, loanId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Failed to update loan status", e);
            return false;
        }
    }

}
