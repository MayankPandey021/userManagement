package com.example.userManagement.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthClientLoginRequest {
    private String clientId;
    private String clientSecret;
}
