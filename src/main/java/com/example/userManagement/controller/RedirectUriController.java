package com.example.userManagement.controller;

import com.example.userManagement.dto.redirectUri.*;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.service.implementation.RedirectUriService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/redirect-uris")
@RequiredArgsConstructor
public class RedirectUriController {

    private final RedirectUriService redirectUriService;

    @GetMapping("/all")
    public ResponseEntity<List<ClientRedirectUrisResponse>> getAllRedirectUris() {
        return ResponseEntity.ok(redirectUriService.getAllActiveRedirectUrisByClientId());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RedirectUriResponse>> getRedirectUris(@PathVariable String clientId) {
        List<RedirectUri> uris = redirectUriService.getRedirectUrisByClientId(clientId);
        List<RedirectUriResponse> responseList = uris.stream().map(uri -> {
            RedirectUriResponse res = new RedirectUriResponse();
            res.setId(uri.getId());
            res.setUri(uri.getUri());
            res.setIsActive(uri.getIsActive());
            res.setIsDeleted(uri.getIsDeleted());
            res.setCreatedAt(uri.getCreatedAt());
            res.setUpdatedAt(uri.getUpdatedAt());
            return res;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/add")
    public ResponseEntity<RedirectUriResponse> create(@Valid @RequestBody CreateRedirectUriRequest request) {
        RedirectUriResponse response = redirectUriService.createRedirectUri(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/update/{clientId}")
    public ResponseEntity<Void> updateRedirectUri(@PathVariable String clientId,
                                                  @Valid @RequestBody UpdateRedirectUriRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        redirectUriService.updateRedirectUri(clientId, request.getOldUri(), request.getNewUri(), username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/client/{clientId}/uri")
    public ResponseEntity<Void> deleteRedirectUriByClientIdAndUri(@PathVariable String clientId,
                                                                  @Valid @RequestBody DeleteRedirectUriRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        redirectUriService.deleteRedirectUri(clientId, request.getUri(), username);
        return ResponseEntity.ok().build();
    }



}