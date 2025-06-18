package com.example.userManagement.controller;

import com.example.userManagement.dto.client.*;
import com.example.userManagement.service.Implementation.OAuthClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class OAuthClientController {

    private final OAuthClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid CreateClientRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // 👈 gets the current logged-in username

        clientService.create(request, username);
        return ResponseEntity.ok("Client created");
    }

    @GetMapping
    public ResponseEntity<List<OAuthClientList>> get() {
        return ResponseEntity.ok(clientService.get());
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<OAuthClientList> getById(@PathVariable String clientId) {
        return ResponseEntity.ok(
                clientService.getById(clientId)
                        .orElseThrow(() -> new RuntimeException("Client not found"))
        );
    }




    @PatchMapping("/{clientId}")
    public ResponseEntity<String> update(@PathVariable String clientId,
                                         @RequestBody UpdateClientRequest request) {
        clientService.update(clientId, request);
        return ResponseEntity.ok("✅ Client updated successfully.");
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> delete(@PathVariable String clientId) {
        clientService.delete(clientId);
        return ResponseEntity.ok().build();
    }
}
