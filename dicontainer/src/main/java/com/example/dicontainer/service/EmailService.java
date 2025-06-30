package com.example.dicontainer.service;

import com.example.dicontainer.interfaces.IEmailService;

public class EmailService implements IEmailService {
    public EmailService(){
        System.out.println("EmailService Created");
    }
    @Override
    public void sendEmail(String to, String message) {
        System.out.println("Sending email to " + to + ": " + message);
    }
}
