// src/main/java/com/example/userManagement/controller/OAuthClientController.java
package com.example.userManagement.controller;

import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.service.OAuthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/oauth-clients")
public class OAuthClientController {

    @Autowired
    private OAuthClientService service;

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody OAuthClient client, Principal principal) {
        OAuthClient created = service.createClient(client, principal.getName());
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PutMapping("/{clientId}")
    public ResponseEntity<?> update(@PathVariable String clientId, @RequestBody OAuthClient client, Principal principal) {
        OAuthClient updated = service.updateClient(clientId, client, principal.getName());
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping
    public List<OAuthClient> listActive() {
        return service.listActiveClients();
    }

    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/{clientId}")
    public ResponseEntity<?> get(@PathVariable String clientId) {
        return service.getClientByClientId(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/{clientId}/reset-secret")
    public ResponseEntity<?> resetSecret(@PathVariable String clientId, @RequestBody String newSecret, Principal principal) {
        service.resetClientSecret(clientId, newSecret, principal.getName());
        return ResponseEntity.ok("Client secret reset");
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/{clientId}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable String clientId, Principal principal) {
        service.deactivateClient(clientId, principal.getName());
        return ResponseEntity.ok("Client deactivated");
    }

    @PreAuthorize("hasAuthority('SCOPE_write')")
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> delete(@PathVariable String clientId, Principal principal) {
        service.deleteClient(clientId, principal.getName());
        return ResponseEntity.ok("Client deleted");
    }
}