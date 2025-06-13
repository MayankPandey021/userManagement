package com.example.userManagement.controller;

import com.example.userManagement.dto.client.*;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.service.OAuthClientService;
import com.example.userManagement.service.OAuthClientService;
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
    public ResponseEntity<List<OAuthClientList>> listClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<OAuthClient> getClientById(@PathVariable String clientId) {
        return ResponseEntity.ok(
                clientService.getClientByClientId(clientId)
                        .orElseThrow(() -> new RuntimeException("Client not found"))
        );
    }

    @PatchMapping("/{clientId}/deactivate")
    public ResponseEntity<Void> deactivateClient(@PathVariable String clientId) {
        clientService.deactivateClient(clientId);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{clientId}/reset-secret")
    public ResponseEntity<Void> resetClientSecret(@PathVariable String clientId,
                                                  @Valid @RequestBody ResetSecretRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String updatedBy = auth.getName(); // 👈 gets the currently logged-in username

        clientService.resetClientSecret(clientId, request.getNewSecret(), updatedBy);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{clientId}")
    public ResponseEntity<String> updateClient(@PathVariable String clientId,
                                               @RequestBody UpdateClientRequest request) {
        clientService.updateClientDetails(clientId, request);
        return ResponseEntity.ok("✅ Client updated successfully.");
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.ok().build();
    }
}
