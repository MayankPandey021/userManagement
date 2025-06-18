package com.example.userManagement.controller;

//import com.example.userManagement.dto.*;
import com.example.userManagement.dto.scopes.CreateScopeRequest;
import com.example.userManagement.dto.scopes.ScopeDetailResponse;
import com.example.userManagement.dto.scopes.ScopeResponse;
import com.example.userManagement.dto.scopes.UpdateScopeRequest;
import com.example.userManagement.service.Implementation.ClientScopeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scopes")
@RequiredArgsConstructor
public class ClientScopeController {

    private final ClientScopeService clientScopeService;



    @PostMapping("/create")
    public ResponseEntity<Void> createScope(@Valid @RequestBody CreateScopeRequest request) {
        clientScopeService.createScope(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<ScopeResponse>> listScopes() {
        return ResponseEntity.ok(clientScopeService.getScopes());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ScopeDetailResponse>> getScopesByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(clientScopeService.getScopesByClientId(clientId));
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateScope(@Valid @RequestBody UpdateScopeRequest request) {
        clientScopeService.updateScope(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{clientId}")
    public ResponseEntity<Void> deleteScope(@PathVariable String clientId) {
        clientScopeService.deleteScope(clientId);
        return ResponseEntity.ok().build();
    }

}
