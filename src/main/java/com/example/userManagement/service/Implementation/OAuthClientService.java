package com.example.userManagement.service.Implementation;

import com.example.userManagement.dto.client.CreateClientRequest;
import com.example.userManagement.dto.client.OAuthClientList;
import com.example.userManagement.dto.client.RedirectUriDto;
import com.example.userManagement.dto.client.UpdateClientRequest;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.exception.ClientNotFoundException;
import com.example.userManagement.service.mapper.ClientMapper;
import com.example.userManagement.repository.OAuthClientRepository;
import com.example.userManagement.service.Interface.IOAuthClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OAuthClientService implements IOAuthClientService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthClientService.class);

    @Autowired
    private OAuthClientRepository clientRepo;

    @Autowired
    private com.example.userManagement.service.Implementation.UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientMapper mapper;

    @Transactional
    public void update(String clientId, @Valid UpdateClientRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String updatedByUsername = auth.getName();

        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        updateEntityFromDto(client, request, updatedByUsername);

        clientRepo.save(client);
    }

    public void create(@Valid CreateClientRequest request, String createdByUsername) {
        if (clientRepo.existsByClientId(request.getClientId())) {
            throw new IllegalArgumentException("ClientId already exists");
        }
        OAuthClient client = mapper.toEntity(request, createdByUsername);
        client.setRedirectUris(mapRedirectUris(request.getRedirectUris(), client, createdByUsername));
        client.setScopes(mapScopes(request.getScopes(), client, createdByUsername));
        clientRepo.save(client);
        logger.info("Client [{}] created by {}", client.getClientId(), createdByUsername);
    }

    public List<OAuthClientList> get() {
        return clientRepo.findAll().stream()
                .filter(client -> Boolean.TRUE.equals(client.getActive()) && Boolean.FALSE.equals(client.getDeleted()))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(String clientId) {
        String updatedByUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        markAsDeleted(client, updatedByUsername);

        clientRepo.save(client);
        logger.info("Client [{}] marked as deleted by {}", clientId, updatedByUsername);
    }

    public Optional<OAuthClientList> getById(String clientId) {
        return clientRepo.findByClientId(clientId)
                .filter(client -> !Boolean.TRUE.equals(client.getDeleted()))
                .map(mapper::toDto);
    }

    // --- Helper methods moved from Mapper ---

    private Set<RedirectUri> mapRedirectUris(List<RedirectUriDto> uris, OAuthClient client, String createdBy) {
        if (uris == null) return Set.of();
        return uris.stream().map(uriStr -> {
            RedirectUri uri = new RedirectUri();
            uri.setId(UUID.randomUUID().toString());
            uri.setUri(uriStr.getUri());
            uri.setClient(client);
            uri.setCreatedAt(LocalDate.now());
            uri.setCreatedBy(createdBy);
            uri.setIsActive(true);
            uri.setIsDeleted(false);
            uri.setUpdatedAt(LocalDate.now());
            uri.setUpdatedBy(createdBy);
            return uri;
        }).collect(Collectors.toSet());
    }

    private Set<ClientScope> mapScopes(List<String> scopes, OAuthClient client, String createdBy) {
        if (scopes == null) return Set.of();
        return scopes.stream().map(scopeStr -> {
            ClientScope scope = new ClientScope();
            scope.setScope(scopeStr);
            scope.setClient(client);
            scope.setCreatedAt(LocalDate.now());
            scope.setCreatedBy(createdBy);
            scope.setIsActive(true);
            scope.setIsDeleted(false);
            scope.setUpdatedAt(LocalDate.now());
            scope.setUpdatedBy(createdBy);
            return scope;
        }).collect(Collectors.toSet());
    }


    private void markAsDeleted(OAuthClient client, String updatedBy) {
        client.setActive(false);
        client.setDeleted(true);
        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedBy);

        client.getRedirectUris().forEach(uri -> {
            uri.setIsActive(false);
            uri.setIsDeleted(true);
            uri.setUpdatedBy(updatedBy);
            uri.setUpdatedAt(LocalDate.now());
        });

        client.getScopes().forEach(scope -> {
            scope.setIsActive(false);
            scope.setIsDeleted(true);
            scope.setUpdatedBy(updatedBy);
            scope.setUpdatedAt(LocalDate.now());
        });
    }

    private void updateEntityFromDto(OAuthClient client, UpdateClientRequest request, String updatedByUsername) {
        if (request.getAuthorizationGrantTypes() != null) {
            client.setAuthorizationGrantTypes(String.join(",", request.getAuthorizationGrantTypes()));
        }
        if (request.getIsActive() != null) {
            client.setActive(request.getIsActive());
        }
        if (request.getClientSecret() != null) {
            client.setClientSecret(passwordEncoder.encode(request.getClientSecret()));
        }
        // Redirect URIs logic
        if (request.getRedirectUris() != null) {
            Map<String, RedirectUri> existingUrisById = client.getRedirectUris().stream()
                    .filter(uri -> uri.getId() != null)
                    .collect(Collectors.toMap(RedirectUri::getId, uri -> uri));
            List<RedirectUri> updatedUriList = new ArrayList<>();
            for (RedirectUriDto dto : request.getRedirectUris()) {
                if (dto.getId() != null && existingUrisById.containsKey(dto.getId())) {
                    RedirectUri existing = existingUrisById.get(dto.getId());
                    existing.setUri(dto.getUri());
                    existing.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
                    existing.setUpdatedBy(updatedByUsername);
                    existing.setUpdatedAt(LocalDate.now());
                    updatedUriList.add(existing);
                } else {
                    RedirectUri newUri = new RedirectUri();
                    newUri.setUri(dto.getUri());
                    newUri.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
                    newUri.setCreatedBy(updatedByUsername);
                    newUri.setCreatedAt(LocalDate.now());
                    newUri.setClient(client);
                    updatedUriList.add(newUri);
                }
            }
            client.setRedirectUris(new HashSet<>(updatedUriList));
        }
        // Scopes logic
        if (request.getScopes() != null) {
            Map<String, ClientScope> existingScopes = client.getScopes().stream()
                    .collect(Collectors.toMap(ClientScope::getScope, scope -> scope));
            Set<String> requestScopes = new HashSet<>(request.getScopes());
            for (String scopeValue : requestScopes) {
                ClientScope scope = existingScopes.get(scopeValue);
                if (scope != null) {
                    scope.setIsActive(request.getIsActive() != null ? request.getIsActive() : scope.getIsActive());
                    scope.setUpdatedBy(updatedByUsername);
                    scope.setUpdatedAt(LocalDate.now());
                } else {
                    ClientScope newScope = new ClientScope();
                    newScope.setScope(scopeValue);
                    newScope.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
                    newScope.setCreatedBy(updatedByUsername);
                    newScope.setCreatedAt(LocalDate.now());
                    newScope.setClient(client);
                    client.getScopes().add(newScope);
                }
            }
            client.getScopes().removeIf(scope -> !requestScopes.contains(scope.getScope()));
        }
        client.setUpdatedBy(updatedByUsername);
        client.setUpdatedAt(LocalDate.now());
    }
}