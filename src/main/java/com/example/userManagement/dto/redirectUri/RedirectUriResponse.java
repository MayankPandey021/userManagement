package com.example.userManagement.dto.redirectUri;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RedirectUriResponse {
    private String id;
    private String uri;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;


}