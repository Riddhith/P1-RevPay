package com.revpay.service;

import com.revpay.model.MoneyRequest;
import com.revpay.repository.MoneyRequestRepository;
import com.revpay.repository.UserRepository;
import com.revpay.repository.WalletRepository;

import java.util.List;

public class MoneyRequestService {

    private MoneyRequestRepository repo = new MoneyRequestRepository();
    private UserRepository userRepo = new UserRepository();
    private WalletRepository walletRepo = new WalletRepository();
    private TransactionService transactionService = new TransactionService();
    private NotificationService notificationService = new NotificationService();

    public void requestMoney(int requesterId, String receiverInput, double amount) {
        int receiverId = userRepo.getUserIdByEmailOrPhone(receiverInput);
        if (receiverId == -1) {
            System.out.println("Receiver not found!");
            return;
        }

        if (requesterId == receiverId) {
            System.out.println("You cannot request money from yourself.");
            return;
        }

        // Save money request
        repo.save(new MoneyRequest(requesterId, receiverId, amount));

        // Notification for receiver
        notificationService.createNotification(receiverId,
                "You have received a money request of ₹" + amount + ".");

        // Notification for requester
        notificationService.createNotification(requesterId,
                "Your money request of ₹" + amount + " is PENDING.");

        System.out.println("Money request sent successfully!");
    }


    public List<MoneyRequest> getPendingRequests(int userId) {
        return repo.getPendingRequests(userId);
    }

    public void acceptRequest(int requestId, int receiverId) {
        MoneyRequest req = repo.getById(requestId);

        if (req == null || !req.getStatus().equals("PENDING")) {
            System.out.println("Invalid or already processed request.");
            return;
        }

        if (req.getReceiverId() != receiverId) {
            System.out.println("Unauthorized action.");
            return;
        }

        double balance = walletRepo.getBalance(receiverId);
        if (balance < req.getAmount()) {
            System.out.println("Insufficient balance to fulfill request.");
            return;
        }

        // Transfer money
        walletRepo.updateBalance(receiverId, balance - req.getAmount());

        double requesterBalance = walletRepo.getBalance(req.getRequesterId());
        walletRepo.updateBalance(req.getRequesterId(), requesterBalance + req.getAmount());

        // ✅ Correct transaction direction:
        // receiverId -> sender (money goes out)
        // requesterId -> receiver (money comes in)
        transactionService.saveTransaction(receiverId, req.getRequesterId(), req.getAmount());

        // Update request status
        repo.updateStatus(requestId, "ACCEPTED");

        // Notifications
        notificationService.createNotification(req.getRequesterId(),
                "Your money request of ₹" + req.getAmount() + " has been ACCEPTED.");
        notificationService.createNotification(receiverId,
                "You accepted the money request of ₹" + req.getAmount());

        System.out.println("Request accepted and payment completed.");
    }


    public void rejectRequest(int requestId, int receiverId) {
        MoneyRequest req = repo.getById(requestId);

        if (req == null || !req.getStatus().equals("PENDING")) {
            System.out.println("Invalid or already processed request.");
            return;
        }

        if (req.getReceiverId() != receiverId) {
            System.out.println("Unauthorized action.");
            return;
        }

        repo.updateStatus(requestId, "REJECTED");

        notificationService.createNotification(req.getRequesterId(),
                "Your money request of ₹" + req.getAmount() + " was REJECTED.");
        notificationService.createNotification(receiverId,
                "You rejected the money request of ₹" + req.getAmount());

        System.out.println("Request rejected.");
    }
}
