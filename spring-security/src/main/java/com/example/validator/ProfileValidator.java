package com.example.validator;

import com.example.entity.Profile;
import com.example.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProfileValidator implements Validator {

    public boolean supports(Class clazz) {
        return Profile.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {

        Profile profile = (Profile) object;

        if (!profile.getNewPassword().equals(profile.getConfirmedPassword())) {
            errors.rejectValue("confirmedPassword", "error.profile.confirmedPassword.not.equal");
        }

        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getPassword().equals(profile.getOldPassword())) {
            errors.rejectValue("oldPassword", "error.profile.oldPassword.not.equal");
        }
    }
}
