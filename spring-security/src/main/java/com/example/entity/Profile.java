package com.example.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
@Data
public class Profile implements Serializable{

    private static final int MINIMUM_PASSWORD_LENGTH = 6;

    private Long id;
    private String userName;
    @NotNull(message = "{error.profile.mail.null}")
    @NotEmpty(message = "{error.profile.email.empty}")
    @Size(max = 50, message = "{error.profile.email.max}")
    private String email;
    private String oldPassword;
    @NotNull(message = "{error.profile.newPassword.null}")
    @NotEmpty(message = "{error.profile.newPassword.empty}")
    @Size(min = 5, max = 20, message = "{error.profile.newPassword.size}")
    private String newPassword;
    private String confirmedPassword;

}
