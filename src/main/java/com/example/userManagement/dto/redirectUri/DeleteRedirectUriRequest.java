// DeleteRedirectUriRequest.java
package com.example.userManagement.dto.redirectUri;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteRedirectUriRequest {
    @NotBlank
    private String uri;

    // Getter & Setter
}