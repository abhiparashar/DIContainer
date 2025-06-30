package com.example.dicontainer;

import com.example.dicontainer.container.SimpleDIContainer;
import com.example.dicontainer.interfaces.*;
import com.example.dicontainer.service.DatabaseService;
import com.example.dicontainer.service.EmailService;
import com.example.dicontainer.service.PaymentService;
import com.example.dicontainer.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DIContainerTest {

    private SimpleDIContainer container;

    @BeforeEach
    void setUp() {
        System.out.println("\n=== SETTING UP NEW CONTAINER ===");
        container = new SimpleDIContainer();
        container.bind(IDatabaseService.class, DatabaseService.class);
        container.bind(IEmailService.class, EmailService.class);
        container.bind(IPaymentService.class, PaymentService.class);
        container.bind(IUserService.class, UserService.class);
        System.out.println("=== CONTAINER SETUP COMPLETE ===\n");
    }

    @Test
    void testSingletonBehavior() {
        System.out.println("🧪 TEST: Singleton Behavior");

        System.out.println("📞 First request for DatabaseService");
        IDatabaseService db1 = container.get(IDatabaseService.class);

        System.out.println("📞 Second request for DatabaseService");
        IDatabaseService db2 = container.get(IDatabaseService.class);

        System.out.println("🔍 Same instance? " + (db1 == db2));
        assertSame(db1, db2, "Should return same instance (singleton)");
        System.out.println("✅ Singleton test passed!\n");
    }

    @Test
    void testDependencyInjection() {
        System.out.println("🧪 TEST: Dependency Injection");

        IUserService userService = container.get(IUserService.class);
        assertNotNull(userService, "UserService should be created");

        System.out.println("🧪 Testing UserService functionality");
        assertDoesNotThrow(() -> {
            userService.createUser("Test User", "test@example.com");
        });
        System.out.println("✅ Dependency injection test passed!\n");
    }

    @Test
    void testMultipleServiceRequests() {
        System.out.println("🧪 TEST: Multiple Service Requests");

        // Request services multiple times within same test
        IDatabaseService db1 = container.get(IDatabaseService.class);
        IEmailService email1 = container.get(IEmailService.class);
        IDatabaseService db2 = container.get(IDatabaseService.class);
        IEmailService email2 = container.get(IEmailService.class);

        // Should be same instances
        assertSame(db1, db2, "DatabaseService should be singleton");
        assertSame(email1, email2, "EmailService should be singleton");

        System.out.println("✅ Multiple requests test passed!\n");
    }

    @Test
    void testComplexDependencyChain() {
        System.out.println("🧪 TEST: Complex Dependency Chain");

        // This will create UserService which depends on Database, Email, and Payment services
        IUserService userService = container.get(IUserService.class);

        // Now request individual services - should reuse existing instances
        IDatabaseService db = container.get(IDatabaseService.class);
        IEmailService email = container.get(IEmailService.class);
        IPaymentService payment = container.get(IPaymentService.class);

        assertNotNull(userService);
        assertNotNull(db);
        assertNotNull(email);
        assertNotNull(payment);

        System.out.println("✅ Complex dependency chain test passed!\n");
    }
}