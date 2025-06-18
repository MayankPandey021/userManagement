package com.example.userManagement.controller;

import com.example.userManagement.dto.user.LoginRequest;
import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.dto.user.UserUpdateRequest;
import com.example.userManagement.dto.user.UserResponseDto;
import com.example.userManagement.service.Implementation.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateRequest r) {
        return ResponseEntity.ok(service.create(r));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest r) {
        return service.login(r.username, r.password)
                .map(u -> ResponseEntity.ok("Login successful"))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestBody UserUpdateRequest request) {
        service.update(id, request); // ✅ Instance call, not static
        return ResponseEntity.ok("✅ User updated successfully.");
    }

    @GetMapping
    public List<UserResponseDto> get() {
        return service.get();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public UserResponseDto getById(@PathVariable Long id) {
        return service.getById(id);
    }


    @PreAuthorize("hasAuthority('SCOPE_write')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
