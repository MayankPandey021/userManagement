package com.example.userManagement.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UpdateClientRequest {

    @NotBlank
    private String clientSecret;

    private List<String> redirectUris;

    private List<String> scopes;

    private Boolean isActive; // For patching active status


    private List<String> authorizationGrantTypes;
}
