package com.example.validator;

import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    public boolean supports(Class clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object object, Errors errors) {

        User user = (User) object;
        if (user.getId()==null && userService.findByUserName(user.getUsername()) != null) {
            errors.rejectValue("userName", "error.user.username.duplicate");
        }

    }
}
