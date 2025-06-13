package com.example.userManagement.dto.scopes;

import lombok.Data;

@Data
public class ScopeResponse {
    private String clientId;
    private String scope;
}