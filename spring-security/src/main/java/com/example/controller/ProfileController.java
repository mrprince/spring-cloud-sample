package com.example.controller;

import com.example.entity.Profile;
import com.example.entity.User;
import com.example.security.CurrentUser;
import com.example.service.MessageLocaleService;
import com.example.service.UserService;
import com.example.validator.ProfileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageLocaleService messages;

    @Autowired
    ProfileValidator validator;

    @GetMapping("/profile")
    public String profile(@CurrentUser User user, @ModelAttribute Profile profile) {
        profile.setUserName(user.getUserName());
        profile.setEmail(user.getEmail());
        return "profile";
    }


    @PostMapping("/profile")
    public String profile(@CurrentUser User user, @Valid @ModelAttribute Profile profile, BindingResult bindingResult, RedirectAttributes redirect) {

        validator.validate(profile, bindingResult);

        if (bindingResult.hasErrors()) {
            return "profile";
        }
        user.setEmail(profile.getEmail());
        user.setPassword(profile.getNewPassword());
        userService.save(user);
        redirect.addFlashAttribute("message",messages.getMessage("profile.form.message.saveSuccessful"));
        return "redirect:/profile";
    }
}
