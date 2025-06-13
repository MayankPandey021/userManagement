package com.example.userManagement.dto.client;

import lombok.Data;

import java.util.List;

@Data
public class OAuthClientList {

    private String clientId;
    private String clientSecret;
    private List<String> authorizationGrantTypes;
    private List<String> redirectUris;
    private List<String> scopes;
}
