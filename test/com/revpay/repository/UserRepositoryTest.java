package com.revpay.repository;

import com.revpay.model.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();

        // Ensure user exists before each test
        User user = new User(
                "JUnit User",
                "junit_user@test.com",
                "9999999999",
                "hashedPassword",
                "1234",
                "PERSONAL"
        );

        // Save user only if not present
        int userId = userRepository.getUserIdByEmailOrPhone("junit_user@test.com");
        if (userId == 0) {
            userRepository.save(user);
        }
    }

    @Test
    @Order(1)
    void testSaveUser() {
        int userId = userRepository.getUserIdByEmailOrPhone("junit_user@test.com");
        assertTrue(userId > 0, "User should be saved successfully");
    }

    @Test
    @Order(2)
    void testValidateLogin() {
        boolean isValid = userRepository.validateLogin(
                "junit_user@test.com",
                "hashedPassword"
        );

        assertTrue(isValid, "Login should be valid for correct credentials");
    }

    @Test
    @Order(3)
    void testGetUserIdByEmailOrPhone() {
        int userId = userRepository.getUserIdByEmailOrPhone(
                "junit_user@test.com"
        );

        assertTrue(userId > 0, "User ID should be returned");
    }
}
