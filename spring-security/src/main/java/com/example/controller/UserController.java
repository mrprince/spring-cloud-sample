package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    @RequestMapping("/users")
    public String gerCurrentLoginUserId(Principal user) {
        return user.getName();
    }

}
