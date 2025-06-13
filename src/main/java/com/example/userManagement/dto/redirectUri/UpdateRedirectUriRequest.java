package com.example.userManagement.dto.redirectUri;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRedirectUriRequest {
    @NotBlank private String oldUri;
    @NotBlank private String newUri;
}