package com.example.security;


import com.example.service.LoginAttemptService;
import com.example.service.MessageLocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LimitLoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private MessageLocaleService messages;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String username = token.getName();

        UserDetails userDetails = null;
        if(username != null) {
            userDetails = userDetailsService.loadUserByUsername(username);
        }

        if(userDetails == null) {
            throw new UsernameNotFoundException(messages.getMessage("login.page.error.userNameNotFound"));
        }else if (!userDetails.isEnabled()){
            throw new DisabledException(messages.getMessage("login.page.error.disabled"));
        }

        String password = userDetails.getPassword();
        if(!password.equals(token.getCredentials())) {
            loginAttemptService.count(username);
            throw new BadCredentialsException(messages.getMessage("login.page.error.badCredentials"));
        }
        loginAttemptService.reset(username);
        return new UsernamePasswordAuthenticationToken(userDetails, password,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
