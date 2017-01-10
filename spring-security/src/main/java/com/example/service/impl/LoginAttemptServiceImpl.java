package com.example.service.impl;

import com.example.dao.LoginAttemptRepository;
import com.example.entity.LoginAttempt;
import com.example.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;

    @Autowired
    LoginAttemptRepository loginAttemptRepository;

    @Override
    public void loginFailed(String username) {
        LoginAttempt loginAttempt = loginAttemptRepository.findByUserName(username).orElse(new LoginAttempt());
        loginAttempt.setUserName(username);
        loginAttempt.setAttempts(loginAttempt.getAttempts() + 1);
        loginAttempt.setLastModified(new Date(System.currentTimeMillis()));
        loginAttemptRepository.save(loginAttempt);
    }

    @Override
    public void loginSucceeded(String username) {
        LoginAttempt loginAttempt = loginAttemptRepository.findByUserName(username).orElse(new LoginAttempt());
        loginAttempt.setUserName(username);
        loginAttempt.setAttempts(0);
        loginAttempt.setLastModified(new Date(System.currentTimeMillis()));
        loginAttemptRepository.save(loginAttempt);
    }

    @Override
    public boolean isBlocked(String username) {
        LoginAttempt loginAttempt = loginAttemptRepository.findByUserName(username).orElse(new LoginAttempt());
        return loginAttempt.getAttempts() >= MAX_ATTEMPTS ? true : false;
    }

}
