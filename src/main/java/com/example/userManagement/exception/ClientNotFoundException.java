package com.example.userManagement.exception;

//used for OAuthClientService
public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String message) {
        super(message);
    }
}
