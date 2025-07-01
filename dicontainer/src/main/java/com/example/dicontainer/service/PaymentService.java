package com.example.dicontainer.service;

import com.example.dicontainer.interfaces.IDatabaseService;
import com.example.dicontainer.interfaces.IPaymentService;

public class PaymentService implements IPaymentService {
    private final IDatabaseService databaseService;
    public PaymentService(IDatabaseService databaseService){
        this.databaseService = databaseService;
        System.out.println("PaymentService Created");
    }
    @Override
    public boolean processPayment(Double amount) {
        System.out.println("Processing payment: $" + amount);
        databaseService.save("Payment of $" + amount);
        return true;
    }
}
