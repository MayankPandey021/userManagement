package com.example.userManagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class UserManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}



    //patch
	// CRUD - 5 apis ,  2 for read - > entireRead and ByDetails , Patch -> comes under update.


	// ToDo :
	// Day 2:
	// 5) I deleted a client (soft delete) but the entry is in the database,
	// now if i want to add/update the same client again, it should not be added/updated or should it?
	//6)UpdateClient  to be fixed.

	//Suggestions :
	// 1) Have Proper naming conventions(files , endpoints , methods).
	// 2) Compartmentalize the code properly.
	// 3) Log when performing CRUD operations(postman)
	// 4) Exception handling
}