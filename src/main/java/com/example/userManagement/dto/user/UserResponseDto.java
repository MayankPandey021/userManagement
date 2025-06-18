package com.example.userManagement.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {


    public Long id;
    public String firstName;
    public String lastName;

    public String username;
    public String createdBy;
    public boolean isActive;
    public boolean isDeleted;

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }



}
