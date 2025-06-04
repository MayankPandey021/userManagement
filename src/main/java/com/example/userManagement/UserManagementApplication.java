package com.example.userManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserManagementApplication.class, args);
		//login using OAUTH 2.1 TOKEN (REFRESH TOKEN ,token) ,
		// SECRET KEY (in environmental variables) ,
		// AES 256 instead of AES.
	}

}
