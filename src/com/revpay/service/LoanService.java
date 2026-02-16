package com.revpay.service;

import com.revpay.repository.LoanRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;

public class LoanService {

    private static final Logger logger =
            LogManager.getLogger(LoanService.class);

    private LoanRepository loanRepo = new LoanRepository();

    // Apply for loan
    public void applyLoan(int businessUserId, double amount) {

        if (amount <= 0) {
            System.out.println("❌ Loan amount must be greater than zero.");
            return;
        }

        loanRepo.applyLoan(businessUserId, amount);

        System.out.println("✅ Loan application submitted successfully!");
        System.out.println("📌 Status: PENDING");
        System.out.println("🏦 Our team will review your request shortly.");

        logger.info("Loan applied by businessUserId {} for amount {}", businessUserId, amount);
    }

    // View loans for business user
    public void viewLoans(int businessUserId) {

        ResultSet rs = loanRepo.getLoansByBusinessUser(businessUserId);

        try {
            boolean found = false;

            System.out.println("\n--- Your Loan Applications ---");
            System.out.printf("%-5s %-10s %-10s %-20s%n",
                    "ID", "AMOUNT", "STATUS", "APPLIED ON");

            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-10.2f %-10s %-20s%n",
                        rs.getInt("loan_id"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"));
            }

            if (!found) {
                System.out.println("ℹ You have no loan applications yet.");
            }

        } catch (Exception e) {
            logger.error("Error fetching loans", e);
            System.out.println("❌ Unable to fetch loan details right now.");
        }
    }

    // Approve / Reject loan (admin simulation)
    public void updateLoanStatus(int loanId, String status) {

        boolean updated = loanRepo.updateLoanStatus(loanId, status);

        if (updated) {
            System.out.println("Loan " + loanId + " has been " + status);
        } else {
            System.out.println("Loan update failed. Check Loan ID.");
        }
    }
}
