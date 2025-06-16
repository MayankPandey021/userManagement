package com.example.userManagement.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    public String firstName;
    public String middleName;
    public String lastName;

    @Email(message = "Invalid email format")
    public String email;

    @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
    public String mobile;

    public String username;

    @Size(min = 5, message = "Password must be at least 5 characters")
    public String password;

    public boolean isActive;
}
