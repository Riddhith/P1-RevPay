package com.revpay.repository;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class WalletRepositoryTest {

    private static WalletRepository walletRepository;
    private static int testUserId;

    @BeforeAll
    static void setup() {
        walletRepository = new WalletRepository();

        // ⚠️ Use an existing user ID from your DB
        // Change if required
        testUserId = 1;
    }

    @Test
    void testCreateWallet() {
        assertDoesNotThrow(() -> walletRepository.createWallet(testUserId),
                "Wallet creation should not throw exception");
    }

    @Test
    void testGetBalance() {
        double balance = walletRepository.getBalance(testUserId);
        assertTrue(balance >= 0, "Balance should be zero or positive");
    }

    @Test
    void testUpdateBalance() {
        boolean updated = walletRepository.updateBalance(testUserId, 500.0);
        assertTrue(updated, "Balance should be updated");

        double newBalance = walletRepository.getBalance(testUserId);
        assertEquals(500.0, newBalance, "Balance should match updated value");
    }
}
