package com.example.userManagement.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateClientRequest {

    @NotEmpty
    private List<String> authorizationGrantTypes;
    private String clientSecret;
    private Boolean isActive;
    private List<RedirectUriDto> redirectUris;
    private List<String> scopes;
}
