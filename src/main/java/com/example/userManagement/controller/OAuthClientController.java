package com.example.userManagement.controller;

import com.example.userManagement.dto.client.*;
import com.example.userManagement.entity.OAuthClient;
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
    public ResponseEntity<?> createClient(@RequestBody @Valid CreateClientRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // 👈 gets the current logged-in username

        clientService.createClient(request, username); // ✅ fixed: two parameters now
        return ResponseEntity.ok("Client created");
    }

    @GetMapping
    public ResponseEntity<List<OAuthClientList>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<OAuthClient> getClientByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(
                clientService.getClientByClientId(clientId)
                        .orElseThrow(() -> new RuntimeException("Client not found"))
        );
    }







    @PatchMapping("/{clientId}")
    public ResponseEntity<String> updateClient(@PathVariable String clientId,
                                               @RequestBody UpdateClientRequest request) {
        clientService.updateClient(clientId, request);
        return ResponseEntity.ok("✅ Client updated successfully.");
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok().build();
    }
}
