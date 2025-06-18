package com.example.userManagement.service.Implementation;

import com.example.userManagement.dto.scopes.CreateScopeRequest;
import com.example.userManagement.dto.scopes.ScopeDetailResponse;
import com.example.userManagement.dto.scopes.ScopeResponse;
import com.example.userManagement.dto.scopes.UpdateScopeRequest;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.service.mapper.ClientScopeMapper;
import com.example.userManagement.repository.ClientScopeRepository;
import com.example.userManagement.repository.OAuthClientRepository;
import com.example.userManagement.service.Interface.IClientScopeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientScopeService implements IClientScopeService {

    private final ClientScopeRepository clientScopeRepository;
    private final OAuthClientRepository oAuthClientRepository;
    private final ClientScopeMapper clientScopeMapper;

    @Transactional
    @Override
    public void createScope(CreateScopeRequest request) {
        if (clientScopeRepository.existsByClient_ClientIdAndScopeAndIsDeletedFalse(request.getClientId(), request.getScope())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Scope already exists for this client");
        }

        OAuthClient client = oAuthClientRepository.findByClientId(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        ClientScope scope = clientScopeMapper.toEntity(request, client);
        clientScopeRepository.save(scope);
    }

    @Override
    public List<ScopeResponse> getScopes() {
        return clientScopeRepository.findAll().stream()
                .filter(scope -> !Boolean.TRUE.equals(scope.getIsDeleted()))
                .map(clientScopeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScopeDetailResponse> getScopesByClientId(String clientId) {
        List<ClientScope> scopes = clientScopeRepository.findByClient_ClientIdAndIsDeletedFalse(clientId);
        if (scopes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No scopes found for this client");
        }

        return scopes.stream()
                .map(this::toScopeDetailResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateScope(UpdateScopeRequest request) {
        ClientScope scope = clientScopeRepository
                .findByClient_ClientIdAndScopeAndIsDeletedFalse(request.getClientId(), request.getOldScope())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Old scope not found for this client"));

        if (clientScopeRepository.existsByClient_ClientIdAndScopeAndIsDeletedFalse(request.getClientId(), request.getNewScope())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "New scope already exists for this client");
        }

        updateScopeEntity(scope, request.getNewScope());
        clientScopeRepository.save(scope);
    }

    @Transactional
    @Override
    public void deleteScope(String clientId) {
        List<ClientScope> scopes = clientScopeRepository.findByClient_ClientIdAndIsDeletedFalse(clientId);
        if (scopes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Scopes not found for client");
        }

        for (ClientScope scope : scopes) {
            markAsDeleted(scope);
            clientScopeRepository.save(scope);
        }
    }

    // -------------------- Helper Methods --------------------

    private void markAsDeleted(ClientScope scope) {
        scope.setIsDeleted(true);
        scope.setIsActive(false);
        scope.setUpdatedAt(LocalDate.now());
    }

    private void updateScopeEntity(ClientScope scope, String newScope) {
        scope.setScope(newScope);
        scope.setUpdatedAt(LocalDate.now());
    }

    private ScopeDetailResponse toScopeDetailResponse(ClientScope scope) {
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
    }
}
