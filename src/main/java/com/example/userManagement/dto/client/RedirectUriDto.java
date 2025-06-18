package com.example.userManagement.dto.client;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedirectUriDto {
    private String id;          // UUID string (optional for update)
    private String uri;         // required
    private Boolean isActive;
}
