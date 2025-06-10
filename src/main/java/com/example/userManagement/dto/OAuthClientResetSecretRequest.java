package com.example.userManagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthClientResetSecretRequest {
    private String clientId;
    private String newSecret;
}
