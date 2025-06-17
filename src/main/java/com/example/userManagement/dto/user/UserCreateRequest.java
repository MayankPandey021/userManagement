package com.example.userManagement.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    @NotBlank(message = "First name is required")
    public String firstName;

    public String middleName;

    @NotBlank(message = "Last name is required")
    public String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Mobile is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
    public String mobile;

    @NotBlank(message = "Username is required")
    public String username;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters")
    public String password;
}
