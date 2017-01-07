package com.example;

import com.github.ulisesbocchio.spring.boot.security.saml.annotation.EnableSAMLSSO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSAMLSSO
public class SalesforceSamlApplication {
	public static void main(String[] args) {
		SpringApplication.run(SalesforceSamlApplication.class, args);
	}
}
