package com.example.userManagement.service;

import com.example.userManagement.dto.*;
import com.example.userManagement.dto.scopes.CreateScopeRequest;
import com.example.userManagement.dto.scopes.ScopeDetailResponse;
import com.example.userManagement.dto.scopes.ScopeResponse;
import com.example.userManagement.dto.scopes.UpdateScopeRequest;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.repository.ClientScopeRepository;
import com.example.userManagement.repository.OAuthClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientScopeService {

    private final ClientScopeRepository clientScopeRepository;
    private final OAuthClientRepository oAuthClientRepository;

    @Transactional
    public void createScope(CreateScopeRequest request) {
        if (clientScopeRepository.existsByClient_ClientIdAndScopeAndIsDeletedFalse(request.getClientId(), request.getScope())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Scope already exists for this client");
        }
        OAuthClient client = oAuthClientRepository.findByClientId(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        ClientScope scope = new ClientScope();
        scope.setClient(client);
        scope.setScope(request.getScope());
        scope.setIsActive(true);
        scope.setIsDeleted(false);
        scope.setCreatedAt(LocalDate.now());
        clientScopeRepository.save(scope);
    }

    public List<ScopeResponse> listScopes() {
        return clientScopeRepository.findAll().stream()
                .filter(scope -> !Boolean.TRUE.equals(scope.getIsDeleted()))
                .map(scope -> {
                    ScopeResponse resp = new ScopeResponse();
                    resp.setClientId(scope.getClient().getClientId());
                    resp.setScope(scope.getScope());
                    return resp;
                }).collect(Collectors.toList());
    }

    public List<ScopeDetailResponse> getScopesByClientId(String clientId) {
        List<ClientScope> scopes = clientScopeRepository.findByClient_ClientIdAndIsDeletedFalse(clientId);
        if (scopes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No scopes found for this client");
        }

        return scopes.stream().map(scope -> {
            ScopeDetailResponse resp = new ScopeDetailResponse();
            resp.setClientId(scope.getClient().getClientId());
            resp.setScope(scope.getScope());
            resp.setCreatedBy(scope.getCreatedBy());
            resp.setUpdatedBy(scope.getUpdatedBy());
            resp.setIsDeleted(scope.getIsDeleted());
            resp.setIsActive(scope.getIsActive());
            resp.setCreatedAt(scope.getCreatedAt());
            resp.setUpdatedAt(scope.getUpdatedAt());
            return resp;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateScope(UpdateScopeRequest request) {
        ClientScope scope = clientScopeRepository
                .findByClient_ClientIdAndScopeAndIsDeletedFalse(request.getClientId(), request.getOldScope())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Old scope not found for this client"));

        if (clientScopeRepository.existsByClient_ClientIdAndScopeAndIsDeletedFalse(request.getClientId(), request.getNewScope())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "New scope already exists for this client");
        }

        scope.setScope(request.getNewScope());
        scope.setUpdatedAt(LocalDate.now());
        clientScopeRepository.save(scope);
    }

    @Transactional
    public void deleteScope(String clientId) {
        List<ClientScope> scopes = clientScopeRepository.findByClient_ClientIdAndIsDeletedFalse(clientId);
        if (scopes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scopes not found for client");
        }

        for (ClientScope scope : scopes) {
            scope.setIsDeleted(true);
            scope.setIsActive(false);  // <- deactivate scope
            scope.setUpdatedAt(LocalDate.now());
            clientScopeRepository.save(scope);
        }
    }

    @Transactional
    public void addScope(CreateScopeRequest request) {
        createScope(request);
    }
}
