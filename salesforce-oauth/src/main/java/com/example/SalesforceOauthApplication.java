package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
@EnableOAuth2Client
@EnableAuthorizationServer
public class SalesforceOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesforceOauthApplication.class, args);
	}
}
