// src/main/java/com/example/userManagement/controller/OAuthClientController.java
package com.example.userManagement.controller;

import com.example.userManagement.dto.OAuthClientCreateRequest;
import com.example.userManagement.dto.OAuthClientDto;
import com.example.userManagement.dto.OAuthClientLoginRequest;
import com.example.userManagement.service.OAuthClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class OAuthClientController {

    @Autowired
    private OAuthClientService service;

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody OAuthClientCreateRequest r) {
        return ResponseEntity.ok(service.createClient(r));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody OAuthClientLoginRequest r) {
        return service.login(r.getClientId(), r.getClientSecret())
                .map(c -> ResponseEntity.ok("Login successful"))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping
    public List<OAuthClientDto> list() {
        return service.listClients();
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/{clientId}")
    public OAuthClientDto get(@PathVariable String clientId) {
        return service.getClient(clientId);
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> delete(@PathVariable String clientId) {
        service.deleteClient(clientId);
        return ResponseEntity.ok("Client deleted successfully");
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/reset-secret")
    public ResponseEntity<?> resetSecret(@RequestBody OAuthClientResetSecretRequest r) {
        service.resetSecret(r.getClientId(), r.getNewSecret());
        return ResponseEntity.ok("Client secret reset");
    }
}