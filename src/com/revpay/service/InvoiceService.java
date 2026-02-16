package com.revpay.service;

import com.revpay.repository.InvoiceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;

public class InvoiceService {

    private static final Logger logger =
            LogManager.getLogger(InvoiceService.class);

    private InvoiceRepository invoiceRepo = new InvoiceRepository();

    // Create invoice
    public void createInvoice(int businessUserId, int customerId, double amount) {

        if (amount <= 0) {
            System.out.println("❌ Invoice amount must be greater than zero.");
            return;
        }

        invoiceRepo.createInvoice(businessUserId, customerId, amount);

        System.out.println("✅ Invoice created successfully!");
        System.out.println("📌 Status: UNPAID");
        logger.info("Invoice created for businessUserId {}", businessUserId);
    }

    // View invoices
    public void viewInvoices(int businessUserId) {

        ResultSet rs = invoiceRepo.getInvoicesByBusinessUser(businessUserId);

        try {
            boolean found = false;

            System.out.println("\n--- Your Invoices ---");
            System.out.printf("%-5s %-10s %-10s %-10s %-20s%n",
                    "ID", "CUSTOMER", "AMOUNT", "STATUS", "CREATED ON");

            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-10d %-10.2f %-10s %-20s%n",
                        rs.getInt("invoice_id"),
                        rs.getInt("customer_id"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"));
            }

            if (!found) {
                System.out.println("ℹ No invoices found.");
            }

        } catch (Exception e) {
            logger.error("Error fetching invoices", e);
            System.out.println("❌ Unable to fetch invoices right now.");
        }
    }

    // Mark invoice paid
    public void markInvoicePaid(int invoiceId) {

        boolean updated = invoiceRepo.markInvoicePaid(invoiceId);

        if (updated) {
            System.out.println("✅ Invoice marked as PAID.");
            System.out.println("💰 Payment successfully received.");
        } else {
            System.out.println("❌ Invoice update failed.");
            System.out.println("ℹ Either invoice does not exist or is already PAID.");
        }
    }
}
