package com.revpay.repository;

import com.revpay.model.MoneyRequest;
import com.revpay.model.User;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoneyRequestRepositoryTest {

    private MoneyRequestRepository repository;
    private UserRepository userRepository;

    private int requesterId;
    private int receiverId;

    @BeforeEach
    void setUp() {
        repository = new MoneyRequestRepository();
        userRepository = new UserRepository();

        // Ensure requester exists
        userRepository.save(new User(
                "Requester",
                "requester@test.com",
                "8888888888",
                "pass",
                "1111",
                "PERSONAL"
        ));

        // Ensure receiver exists
        userRepository.save(new User(
                "Receiver",
                "receiver@test.com",
                "7777777777",
                "pass",
                "2222",
                "PERSONAL"
        ));

        requesterId = userRepository.getUserIdByEmailOrPhone("requester@test.com");
        receiverId = userRepository.getUserIdByEmailOrPhone("receiver@test.com");

        // Insert money request
        repository.save(new MoneyRequest(
                requesterId,
                receiverId,
                500.0
        ));
    }

    @Test
    void testSaveMoneyRequest() {
        List<MoneyRequest> requests = repository.getPendingRequests(receiverId);
        assertFalse(requests.isEmpty(), "Money request should be saved");
    }

    @Test
    void testGetPendingRequests() {
        List<MoneyRequest> requests = repository.getPendingRequests(receiverId);
        assertTrue(requests.size() > 0, "Pending requests should exist");
    }

    @Test
    void testUpdateStatus() {
        List<MoneyRequest> requests = repository.getPendingRequests(receiverId);
        MoneyRequest req = requests.get(0);

        repository.updateStatus(req.getRequestId(), "ACCEPTED");

        MoneyRequest updated = repository.getById(req.getRequestId());
        assertEquals("ACCEPTED", updated.getStatus());
    }
}
