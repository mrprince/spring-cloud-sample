package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @ModelAttribute("navSection")
    public String module() {
        return "home";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

}
