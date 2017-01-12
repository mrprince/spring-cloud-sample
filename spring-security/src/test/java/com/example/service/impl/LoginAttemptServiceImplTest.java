package com.example.service.impl;

import com.example.service.LoginAttemptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginAttemptServiceImplTest {

    @Autowired
    LoginAttemptService loginAttemptService;

    @Test
    public void test() {
        String userName = "admin";
        for (int i = 0; i < 3; i++) {
            loginAttemptService.count(userName);
        }
        loginAttemptService.reset(userName);
    }


}