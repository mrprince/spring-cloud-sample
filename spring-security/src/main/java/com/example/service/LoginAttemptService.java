package com.example.service;

public interface LoginAttemptService {
    void count(String username);
    void reset(String username);
}
