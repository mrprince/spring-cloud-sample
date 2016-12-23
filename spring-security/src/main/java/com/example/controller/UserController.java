package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/users")
@Controller
@Log4j
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getUserCreatePage() {
        if (log.isDebugEnabled()) {
            log.debug("Getting user create form");
        }
        return "users/user";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String userCreate(@ModelAttribute("user") User user) {
        if (log.isDebugEnabled()) {
            log.debug("User create");
        }
        userService.create(user);
        return "redirect:/users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping
    public ModelAndView userList(Model model) {
        if (log.isDebugEnabled()) {
            log.debug("User create");
        }
        return new ModelAndView("/users/list", "list", userService.getAllUsers());
    }


}
