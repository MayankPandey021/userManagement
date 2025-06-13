package com.example.userManagement.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class CreateClientRequest {
    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;

    private List<String> redirectUris;

    private List<String> scopes;

    private List<String> authorizationGrantTypes;

    private String createdBy;
}
