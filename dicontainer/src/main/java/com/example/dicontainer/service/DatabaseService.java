package com.example.dicontainer.service;

import com.example.dicontainer.interfaces.IDatabaseService;

public class DatabaseService implements IDatabaseService {

    public DatabaseService(){
       System.out.println("DatabaseService created");
    }
    @Override
    public void save(String data) {
        System.out.println("Saving to database: " + data);
    }

    @Override
    public String findById(String id) {
        return "User data for ID: " + id;
    }
}
