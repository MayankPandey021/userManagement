package com.example.userManagement.dto.scopes;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreateScopeRequest {
    @NotBlank(message = "Client ID must not be blank")
    private String clientId;

    @NotBlank(message = "Scope must not be blank")
    private String scope;
}