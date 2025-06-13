package com.example.userManagement.dto.redirectUri;

import lombok.Data;

import java.util.List;

@Data
public class ClientRedirectUrisResponse {
    private String clientId;
    private List<String> redirectUris;
}
