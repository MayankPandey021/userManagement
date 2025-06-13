package com.example.userManagement.dto.scopes;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateScopeRequest {

    @NotBlank(message = "Client ID must not be blank")
    private String clientId;

    @NotBlank(message = "Old scope must not be blank")
    private String oldScope;

    @NotBlank(message = "New scope must not be blank")
    private String newScope;
}



