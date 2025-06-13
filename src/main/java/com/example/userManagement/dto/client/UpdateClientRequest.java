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

    private String updatedBy;

    private List<String> authorizationGrantTypes; // ✅ Added this field
}
