package com.example.userManagement.dto.redirectUri;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRedirectUriRequest {
    @NotBlank
    private String uri;

    @NotBlank
    private String clientId; // 👈 this is required to link the URI to a client
}