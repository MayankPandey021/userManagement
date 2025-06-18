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
    public ResponseEntity<Void> create(@Valid @RequestBody CreateScopeRequest request) {
        clientScopeService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<ScopeResponse>> get() {
        return ResponseEntity.ok(clientScopeService.get());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ScopeDetailResponse>> getById(@PathVariable String clientId) {
        return ResponseEntity.ok(clientScopeService.getById(clientId));
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> update(@Valid @RequestBody UpdateScopeRequest request) {
        clientScopeService.update(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{clientId}")
    public ResponseEntity<Void> delete(@PathVariable String clientId) {
        clientScopeService.delete(clientId);
        return ResponseEntity.ok().build();
    }

}
