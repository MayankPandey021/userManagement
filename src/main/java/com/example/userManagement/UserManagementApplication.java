package com.example.userManagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class UserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}



	// CRUD - 5 apis ,  2 for read - > entireRead and ByDetails , Patch -> comes under update.
    // 1) Get  , 2) GetById , 3) Create , 4) Update/Put , 5) Delete


	// ToDo :


	//Suggestions :
	// 1) Have Proper naming conventions(files , endpoints , methods).
	// 2) Compartmentalize the code properly.
	// 3) Log when performing CRUD operations(postman)
	// 4) Exception handling


}