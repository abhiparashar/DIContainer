package com.example.dicontainer.controller;

import com.example.dicontainer.container.SimpleDIContainer;
import com.example.dicontainer.interfaces.*;
import com.example.dicontainer.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-di")
    public String testDI() {
        StringBuilder result = new StringBuilder();
        result.append("=== Testing Custom DI Container ===\n\n");

        try {
            // Create container
            SimpleDIContainer container = new SimpleDIContainer();

            // Register bindings
            container.bind(IDatabaseService.class, DatabaseService.class);
            container.bind(IEmailService.class, EmailService.class);
            container.bind(IPaymentService.class, PaymentService.class);
            container.bind(IUserService.class, UserService.class);

            // Print dependency graph
            container.printDependencyGraph();

            // Initialize all services
            container.initializeAll();

            // Test the services
            IUserService userService = container.get(IUserService.class);
            userService.createUser("John Doe", "john@example.com");

            result.append("✅ DI Container test completed successfully!");

        } catch (Exception e) {
            result.append("❌ Error: ").append(e.getMessage());
        }

        return result.toString().replace("\n", "<br>");
    }
}
