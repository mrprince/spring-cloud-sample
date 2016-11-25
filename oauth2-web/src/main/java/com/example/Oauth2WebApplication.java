package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@EnableZuulProxy
public class Oauth2WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2WebApplication.class, args);
    }

    @RestController
    protected static class UserController{
        @RequestMapping("/name")
        public String gerCurrentLoginUserId(Principal user){
            return user.getName();
        }
    }

    @Configuration
    @EnableOAuth2Sso
    protected static class LoginConfig extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.logout().and()
                    .authorizeRequests()
                    .antMatchers("/index.html", "/home.html", "/", "/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }
    }

}
