package com.example.userManagement.controller;

import com.example.userManagement.dto.user.LoginRequest;
import com.example.userManagement.dto.user.ResetPasswordRequest;
import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.entity.User;
import com.example.userManagement.service.UserService;
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
        return ResponseEntity.ok(service.createUser(r));
    }

    @PostMapping("/login") // Usually public
    public ResponseEntity<?> login(@RequestBody LoginRequest r) {
        return service.login(r.username, r.password)
                .map(u -> ResponseEntity.ok("Login successful"))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping
    public List<User> list() {
        return service.listUsers();
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return service.getUser(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody ResetPasswordRequest r) {
        service.resetPassword(r.username, r.newPassword);
        return ResponseEntity.ok("Password reset");
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
