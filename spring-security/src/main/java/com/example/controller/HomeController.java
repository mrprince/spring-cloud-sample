package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/login")
    public ModelAndView getLoginForm(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout) {

        String message = "";
        if (error != null) {
            message = "Incorrect username or password !";
        } else if (logout != null) {
            message = "Logout successful !";
        }
        return new ModelAndView("login", "message", message);
    }


}
