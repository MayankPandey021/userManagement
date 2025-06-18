package com.example.userManagement.controller;

import com.example.userManagement.dto.redirectUri.*;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.service.Implementation.RedirectUriService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.userManagement.dto.redirectUri.UpdateRedirectUriRequest;
import java.util.List;
import java.util.stream.Collectors;
import com.example.userManagement.service.mapper.RedirectUriMapper;

@RestController
@RequestMapping("/api/redirect-uris")
@RequiredArgsConstructor
public class RedirectUriController {

    private final RedirectUriService redirectUriService;
    private final RedirectUriMapper redirectUriMapper;

    @GetMapping("/all")
    public ResponseEntity<List<ClientRedirectUrisResponse>> get() {
        return ResponseEntity.ok(redirectUriService.get());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RedirectUriResponse>> getById(@PathVariable String clientId) {
        List<RedirectUri> uris = redirectUriService.getById(clientId);
        List<RedirectUriResponse> responseList = uris.stream()
                .map(redirectUriMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/add")
    public ResponseEntity<RedirectUriResponse> create(@Valid @RequestBody CreateRedirectUriRequest request) {
        RedirectUriResponse response = redirectUriService.create(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/update/{clientId}")
    public ResponseEntity<Void> update(@PathVariable String clientId,
                                       @Valid @RequestBody UpdateRedirectUriRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        redirectUriService.update(clientId, request.getOldUri(), request.getNewUri(), username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/client/{clientId}/uri")
    public ResponseEntity<Void> delete(@PathVariable String clientId,
                                       @Valid @RequestBody DeleteRedirectUriRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        redirectUriService.delete(clientId, request.getUri(), username);
        return ResponseEntity.ok().build();
    }



}