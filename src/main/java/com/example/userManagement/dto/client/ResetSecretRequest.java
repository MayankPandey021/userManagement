package com.example.userManagement.dto.client;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetSecretRequest {
    @NotBlank private String newSecret;
}
