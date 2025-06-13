package com.example.userManagement.dto.scopes;

import lombok.Data;

import java.time.LocalDate;


@Data
public class ScopeDetailResponse {
    private String clientId;
    private String scope;
    private String createdBy;
    private String updatedBy;
    private Boolean isDeleted;
    private Boolean isActive;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}