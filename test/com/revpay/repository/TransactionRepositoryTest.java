package com.revpay.repository;

import com.revpay.model.Transaction;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {

    private static TransactionRepository transactionRepository;

    // ⚠️ Use existing user IDs from your database
    private static int senderId = 1;
    private static int receiverId = 2;

    @BeforeAll
    static void setup() {
        transactionRepository = new TransactionRepository();
    }

    @Test
    void testSaveTransaction() {
        Transaction txn = new Transaction(
                senderId,
                receiverId,
                150.0,
                "SEND",
                "SUCCESS"
        );

        assertDoesNotThrow(() ->
                        transactionRepository.save(txn),
                "Saving transaction should not throw exception"
        );
    }

    @Test
    void testGetTransactionsByUser() {
        List<Transaction> senderTxns =
                transactionRepository.getTransactionsByUser(senderId);

        assertNotNull(senderTxns, "Transaction list should not be null");
        assertTrue(senderTxns.size() > 0,
                "Sender should have at least one transaction");

        Transaction txn = senderTxns.get(0);

        assertTrue(
                txn.getSenderId() == senderId || txn.getReceiverId() == senderId,
                "Transaction must involve the user"
        );

        assertNotNull(txn.getType(), "Transaction type should not be null");
        assertNotNull(txn.getStatus(), "Transaction status should not be null");
        assertNotNull(txn.getCreatedAt(), "CreatedAt should not be null");
    }
}
