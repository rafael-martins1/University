package com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.roytuts.spring.boot.security.form.based.jdbc.userdetailsservice.auth")
public class SpringSecurityFormBasedJdbcUserDetailsServiceAuthApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityFormBasedJdbcUserDetailsServiceAuthApp.class, args);
                
                System.out.println("\nOPEN:  https://localhost:8443/");
	}

}
