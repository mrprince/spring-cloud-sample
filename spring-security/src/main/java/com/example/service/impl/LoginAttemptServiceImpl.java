package com.example.service.impl;

import com.example.dao.LoginAttemptRepository;
import com.example.entity.LoginAttempt;
import com.example.entity.User;
import com.example.service.LoginAttemptService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;

    @Autowired
    private LoginAttemptRepository loginAttemptRepository;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void count(String username) {
        // increase
        LoginAttempt loginAttempt = loginAttemptRepository.findByUserName(username).orElse(new LoginAttempt());
        loginAttempt.setUserName(username);
        loginAttempt.setAttempts(loginAttempt.getAttempts() + 1);
        loginAttempt.setLastModified(new Date(System.currentTimeMillis()));
        loginAttemptRepository.save(loginAttempt);

        //disable account
        if(loginAttempt.getAttempts() >= MAX_ATTEMPTS ){
            User user = userService.findByUserName(username);
            user.setEnabled(false);
            userService.save(user);
        }
    }

    @Override
    public void reset(String username) {
        LoginAttempt loginAttempt = loginAttemptRepository.findByUserName(username).orElse(new LoginAttempt());
        loginAttempt.setUserName(username);
        loginAttempt.setAttempts(0);
        loginAttempt.setLastModified(new Date(System.currentTimeMillis()));
        loginAttemptRepository.save(loginAttempt);
    }


}
