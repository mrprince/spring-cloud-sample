package com.example.service;

public interface LoginAttemptService {
    void loginFailed(String username);
    void loginSucceeded(String username);
    boolean isBlocked(String username);
}
