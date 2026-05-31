package com.example.ordermanagement;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for integration tests using Testcontainers
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class IntegrationTestBase {
}

