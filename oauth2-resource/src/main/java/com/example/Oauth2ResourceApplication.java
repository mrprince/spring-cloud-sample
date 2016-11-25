package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableResourceServer
public class Oauth2ResourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2ResourceApplication.class, args);
	}

    @RestController
    public class helloController{
        @RequestMapping("/")
        public String home() {
            return "Hello World";
        }

    }


}
