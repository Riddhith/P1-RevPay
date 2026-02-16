package com.revpay.main;

import com.revpay.model.MoneyRequest;
import com.revpay.model.Transaction;
import com.revpay.service.MoneyRequestService;
import com.revpay.service.NotificationService;
import com.revpay.service.PaymentService;
import com.revpay.service.UserService;
import com.revpay.service.WalletService;
import com.revpay.service.TransactionService;
import com.revpay.service.LoanService;
import com.revpay.service.InvoiceService;
import java.util.List;
import java.util.Scanner;

public class RevPayApp {

    static UserService service = new UserService();
    static PaymentService paymentService = new PaymentService();
    static WalletService walletService = new WalletService();
    static TransactionService transactionService = new TransactionService();
    static MoneyRequestService moneyRequestService = new MoneyRequestService();
    static NotificationService notificationService = new NotificationService();
    static LoanService loanService = new LoanService();
    static InvoiceService invoiceService = new InvoiceService();
    static String loggedInAccountType = null;
    static int loggedInUserId = -1;
    static String loggedInEmail = null;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== RevPay ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> register(sc);
                case 2 -> login(sc);
                case 3 -> System.exit(0);
            }
        }
    }

    private static void register(Scanner sc) {
        System.out.print("Full Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Phone: ");
        String phone = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        System.out.print("Transaction PIN: ");
        String pin = sc.nextLine();
        System.out.print("Account Type (PERSONAL/BUSINESS): ");
        String type = sc.nextLine();

        boolean status = service.register(name, email, phone, pass, pin, type);
        System.out.println(status ? "Registration Successful!" : "Registration Failed!");
    }

    private static void login(Scanner sc) {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        boolean status = service.login(email, pass);
        if (status) {
            loggedInUserId = service.getUserId(email);
            loggedInEmail = email;
            loggedInAccountType = service.getAccountType(email); // 👈 NEW

            System.out.println("Login Successful! Account Type: " + loggedInAccountType);
            walletMenu(sc);
        } else {
            System.out.println("Invalid Credentials!");
        }
    }



    private static void walletMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- Wallet Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Add Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Send Money");
            System.out.println("5. View Transaction History");
            System.out.println("6. Request Money");
            System.out.println("7. View Money Requests");
            System.out.println("8. View Notifications");

            // 👇 BUSINESS-ONLY OPTIONS (EXTENSION)
            if ("BUSINESS".equalsIgnoreCase(loggedInAccountType)) {
                System.out.println("9. Create Invoice (Business)");
                System.out.println("10. View Invoices (Business)");
                System.out.println("11. Mark Invoice Paid (Business)");
                System.out.println("12. Apply for Business Loan");
                System.out.println("13. View Loan Applications");
            }

            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {

                case 1 -> System.out.println("Balance: ₹" +
                        walletService.checkBalance(loggedInUserId));

                case 2 -> {
                    System.out.print("Amount: ");
                    double amt = sc.nextDouble();
                    sc.nextLine();
                    walletService.addMoney(loggedInUserId, amt);
                }

                case 3 -> {
                    System.out.print("Amount: ");
                    double amt = sc.nextDouble();
                    sc.nextLine();
                    walletService.withdrawMoney(loggedInUserId, amt);
                }

                case 4 -> sendMoney(sc);
                case 5 -> viewTransactionHistory();
                case 6 -> requestMoney(sc);
                case 7 -> viewMoneyRequests(sc);
                case 8 -> viewNotifications();

                // 👇 BUSINESS FEATURES
                case 9 -> {
                    if (isBusiness()) {
                        System.out.print("Enter Customer ID: ");
                        int customerId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter Amount: ");
                        double amount = sc.nextDouble();
                        sc.nextLine();

                        invoiceService.createInvoice(loggedInUserId, customerId, amount);
                    }
                }

                case 10 -> {
                    if (isBusiness()) {
                        invoiceService.viewInvoices(loggedInUserId);
                    }
                }

                case 11 -> {
                    if (isBusiness()) {
                        System.out.print("Enter Invoice ID to mark PAID: ");
                        int invoiceId = sc.nextInt();
                        sc.nextLine();

                        invoiceService.markInvoicePaid(invoiceId);
                    }
                }

                case 12 -> {
                    if (isBusiness()) {
                        System.out.print("Enter loan amount: ");
                        double amount = sc.nextDouble();
                        sc.nextLine();
                        loanService.applyLoan(loggedInUserId, amount);
                    }
                }

                case 13 -> {
                    if (isBusiness()) {
                        loanService.viewLoans(loggedInUserId);
                    }
                }


                case 0 -> {
                    loggedInUserId = -1;
                    loggedInEmail = null;
                    loggedInAccountType = null;
                    System.out.println("Logged out!");
                    return;
                }

                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void sendMoney(Scanner sc) {
        System.out.print("Enter receiver Email/Phone: ");
        String receiver = sc.nextLine();

        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        boolean status = paymentService.sendMoney(loggedInUserId, receiver, amount);
        System.out.println(status ? "Money sent successfully!" : "Transaction failed!");
    }
    private static void viewTransactionHistory() {
        List<Transaction> transactions = transactionService.getUserTransactions(loggedInUserId);

        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        System.out.println("\n========== Transaction History ==========");
        System.out.printf("%-5s %-10s %-10s %-10s %-10s %-20s%n",
                "ID", "FROM", "TO", "AMOUNT", "TYPE", "DATE");

        for (Transaction t : transactions) {

            // Decide SEND or RECEIVE based on logged-in user
            String type="";
            if (t.getSenderId() == loggedInUserId) {
                type = "SEND";
            } else if (t.getReceiverId() == loggedInUserId) {
                type = "RECEIVE";
            }

            System.out.printf("%-5d %-10d %-10d %-10.2f %-10s %-20s%n",
                    t.getTxnId(),
                    t.getSenderId(),
                    t.getReceiverId(),
                    t.getAmount(),
                    type,
                    t.getCreatedAt());
        }
    }

    private static void requestMoney(Scanner sc) {
        System.out.print("Enter receiver Email/Phone: ");
        String input = sc.nextLine();

        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        moneyRequestService.requestMoney(loggedInUserId, input, amount);
    }

    private static void viewMoneyRequests(Scanner sc) {
        List<MoneyRequest> requests = moneyRequestService.getPendingRequests(loggedInUserId);

        if (requests.isEmpty()) {
            System.out.println("No pending money requests.");
            return;
        }

        System.out.println("\n--- Pending Money Requests ---");
        System.out.printf("%-5s %-10s %-10s %-10s %-10s%n",
                "ID", "FROM", "TO", "AMOUNT", "STATUS");

        for (MoneyRequest r : requests) {
            System.out.printf("%-5d %-10d %-10d %-10.2f %-10s%n",
                    r.getRequestId(),
                    r.getRequesterId(),
                    r.getReceiverId(),
                    r.getAmount(),
                    r.getStatus());
        }

        System.out.print("\nEnter Request ID to process (0 to cancel): ");
        int id = sc.nextInt();
        sc.nextLine();

        if (id == 0) return;

        System.out.println("1. Accept");
        System.out.println("2. Reject");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            moneyRequestService.acceptRequest(id, loggedInUserId);
        } else if (choice == 2) {
            moneyRequestService.rejectRequest(id, loggedInUserId);
        }
    }

    private static void viewNotifications() {
        var notifications = notificationService.getNotifications(loggedInUserId);

        if (notifications.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }

        System.out.println("\n--- Notifications ---");
        for (var n : notifications) {
            System.out.println("[" + (n.isRead() ? "READ" : "NEW") + "] " +
                    n.getCreatedAt() + " - " + n.getMessage());
        }
    }

    private static boolean isBusiness() {
        if (!"BUSINESS".equalsIgnoreCase(loggedInAccountType)) {
            System.out.println("❌ Business account required.");
            return false;
        }
        return true;
    }
}
