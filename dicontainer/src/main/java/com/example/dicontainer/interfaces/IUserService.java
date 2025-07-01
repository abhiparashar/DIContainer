package com.example.dicontainer.interfaces;

public interface IUserService {
    void createUser(String name, String email);
    void notifyUser(String userId, String message);
}
