package com.revpay.service;

import com.revpay.model.User;
import com.revpay.repository.InvoiceRepository;
import com.revpay.repository.LoanRepository;
import com.revpay.repository.PaymentAcceptanceRepository;
import com.revpay.repository.WalletRepository;

public class BusinessService {

    private final LoanRepository loanRepository = new LoanRepository();
    private final InvoiceRepository invoiceRepository = new InvoiceRepository();
    private final PaymentAcceptanceRepository paymentRepository = new PaymentAcceptanceRepository();
    private final WalletRepository walletRepository = new WalletRepository();

    // 🔒 Centralized check
    private void validateBusinessUser(User user) {
        if (user == null || !"BUSINESS".equalsIgnoreCase(user.getAccountType())) {
            throw new RuntimeException("Access denied: Business users only");
        }
    }

    // ==========================
    // 1️⃣ Loan Application
    // ==========================
    public void applyForLoan(User user, double amount) {
        validateBusinessUser(user);
        loanRepository.applyLoan(user.getUserId(), amount);
    }

    // ==========================
    // 2️⃣ Invoice Management
    // ==========================
    public void createInvoice(User user, int customerId, double amount) {
        validateBusinessUser(user);
        invoiceRepository.createInvoice(user.getUserId(), customerId, amount);
    }

    // ==========================
    // 3️⃣ Payment Acceptance
    // ==========================
    public void acceptPayment(User user, int payerId, double amount) {
        validateBusinessUser(user);

        paymentRepository.acceptPayment(
                user.getUserId(),
                payerId,
                amount
        );

        // Credit wallet after payment
        double currentBalance = walletRepository.getBalance(user.getUserId());
        walletRepository.updateBalance(
                user.getUserId(),
                currentBalance + amount
        );
    }
}
