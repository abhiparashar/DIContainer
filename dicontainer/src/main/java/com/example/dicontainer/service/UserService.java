package com.example.dicontainer.service;

import com.example.dicontainer.interfaces.IDatabaseService;
import com.example.dicontainer.interfaces.IEmailService;
import com.example.dicontainer.interfaces.IPaymentService;
import com.example.dicontainer.interfaces.IUserService;

public class UserService implements IUserService {
    private final IDatabaseService databaseService;
    private final IEmailService emailService;
    private final IPaymentService paymentService;

    public UserService(IDatabaseService databaseService, IEmailService emailService, IPaymentService paymentService) {
        this.databaseService = databaseService;
        this.emailService = emailService;
        this.paymentService = paymentService;
    }

    @Override
    public void createUser(String name, String email) {
        System.out.println("Creating user: " + name);
        databaseService.save("User: " + name + ", Email: " + email);
        emailService.sendEmail(email, "Welcome " + name + "!");
    }

    @Override
    public void notifyUser(String userId, String message) {
        String userData = databaseService.findById(userId);
        emailService.sendEmail("user@email.com", message);
    }
}
