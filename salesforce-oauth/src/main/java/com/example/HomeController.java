package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController {

    @RequestMapping("/")
    public ModelAndView home(Principal principal) {
        ModelAndView homeView = new ModelAndView("index");
        if (principal != null)
            homeView.addObject("userId", principal.getName());
        return homeView;
    }


}