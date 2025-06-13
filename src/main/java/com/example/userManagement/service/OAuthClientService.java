package com.example.userManagement.service;

import com.example.userManagement.dto.client.CreateClientRequest;
import com.example.userManagement.dto.client.OAuthClientList;
import com.example.userManagement.dto.client.UpdateClientRequest;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.exception.ClientNotFoundException;
import com.example.userManagement.repository.OAuthClientRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OAuthClientService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthClientService.class);

    @Autowired
    private OAuthClientRepository clientRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void updateClientDetails(String clientId, @Valid UpdateClientRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String updatedByUsername = auth.getName(); // 👈 fetch logged-in username

        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        String updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();

        if (request.getClientSecret() != null) {
            client.setClientSecret(passwordEncoder.encode(request.getClientSecret()));
        }

        if (request.getAuthorizationGrantTypes() != null) {
            client.setAuthorizationGrantTypes(String.valueOf(request.getAuthorizationGrantTypes()));
        }



        client.setUpdatedAt(LocalDate.now()); // always update timestamp
        client.setUpdatedBy(updatedByUsername);

        if (request.getRedirectUris() != null) {
            Set<RedirectUri> redirectUris = request.getRedirectUris().stream().map(uriStr -> {
                RedirectUri uri = new RedirectUri();
                uri.setId(UUID.randomUUID().toString());
                uri.setUri(uriStr);
                uri.setClient(client);
                uri.setCreatedAt(LocalDate.now());
                uri.setCreatedBy(updatedBy);
                uri.setIsActive(true);
                uri.setIsDeleted(false);
                uri.setUpdatedBy(updatedByUsername);
                uri.setUpdatedAt(LocalDate.now());
                return uri;
            }).collect(Collectors.toSet());
            client.getRedirectUris().clear();
            client.getRedirectUris().addAll(redirectUris);
        }

        if (request.getScopes() != null) {
            Set<ClientScope> scopes = request.getScopes().stream().map(scopeStr -> {
                ClientScope scope = new ClientScope();
                scope.setScope(scopeStr);
                scope.setClient(client);
                scope.setCreatedAt(LocalDate.now());
                scope.setCreatedBy(updatedBy);
                scope.setIsActive(true);
                scope.setIsDeleted(false);
                scope.setUpdatedBy(updatedByUsername);
                scope.setUpdatedAt(LocalDate.now());
                return scope;
            }).collect(Collectors.toSet());
            client.getScopes().clear();
            client.getScopes().addAll(scopes);
        }

        clientRepo.save(client);
    }


    public OAuthClient createClient(@Valid CreateClientRequest request, String createdByUsername) {
        if (clientRepo.existsByClientId(request.getClientId())) {
            throw new IllegalArgumentException("ClientId already exists");
        }

        OAuthClient client = new OAuthClient();
        client.setClientId(request.getClientId());
        client.setClientSecret(passwordEncoder.encode(request.getClientSecret()));
        client.setAuthorizationGrantTypes(String.valueOf(request.getAuthorizationGrantTypes()));
        client.setCreatedAt(LocalDate.now());
        client.setCreatedBy(createdByUsername);
        client.setActive(true);
        client.setDeleted(false);

        // Set redirect URIs
        if (request.getRedirectUris() != null) {
            Set<RedirectUri> uris = request.getRedirectUris().stream().map(uriStr -> {
                RedirectUri uri = new RedirectUri();
                uri.setId(UUID.randomUUID().toString());
                uri.setUri(uriStr);
                uri.setClient(client);
                uri.setCreatedAt(LocalDate.now());
                uri.setCreatedBy(createdByUsername);
                uri.setIsActive(true);
                uri.setIsDeleted(false);
                return uri;
            }).collect(Collectors.toSet());
            client.setRedirectUris(uris);
        }

        // Set scopes
        if (request.getScopes() != null) {
            Set<ClientScope> scopes = request.getScopes().stream().map(scopeStr -> {
                ClientScope scope = new ClientScope();
                scope.setScope(scopeStr);
                scope.setClient(client);
                scope.setCreatedAt(LocalDate.now());
                scope.setCreatedBy(createdByUsername);
                scope.setIsActive(true);
                scope.setIsDeleted(false);
                return scope;
            }).collect(Collectors.toSet());
            client.setScopes(scopes);
        }

        OAuthClient saved = clientRepo.save(client);
        logger.info("Client [{}] created by {}", client.getClientId(), createdByUsername);
        return saved;
    }



    public List<OAuthClientList> getClients() {
        return clientRepo.findAll().stream()
                .filter(client -> Boolean.TRUE.equals(client.getActive()) && Boolean.FALSE.equals(client.getDeleted()))
                .map(client -> {
                    OAuthClientList dto = new OAuthClientList();
                    dto.setClientId(client.getClientId());
                    dto.setClientSecret(client.getClientSecret());
                    dto.setAuthorizationGrantTypes(Collections.singletonList(client.getAuthorizationGrantTypes()));

                    dto.setRedirectUris(
                            client.getRedirectUris().stream()
                                    .filter(uri -> Boolean.TRUE.equals(uri.getIsActive()) && !Boolean.TRUE.equals(uri.getIsDeleted()))
                                    .map(RedirectUri::getUri)
                                    .collect(Collectors.toList())
                    );

                    dto.setScopes(
                            client.getScopes().stream()
                                    .filter(scope -> Boolean.TRUE.equals(scope.getIsActive()) && !Boolean.TRUE.equals(scope.getIsDeleted()))
                                    .map(ClientScope::getScope)
                                    .collect(Collectors.toList())
                    );

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void deactivateClient(String clientId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String updatedByUsername = auth.getName(); // 👈 fetch logged-in username

        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        client.setActive(false);
        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedByUsername);

        for (RedirectUri uri : client.getRedirectUris()) {
            uri.setIsActive(false);
            uri.setUpdatedBy(updatedByUsername);
            uri.setUpdatedAt(LocalDate.now());
        }

        for (ClientScope scope : client.getScopes()) {
            scope.setIsActive(false);
            scope.setUpdatedBy(updatedByUsername);
            scope.setUpdatedAt(LocalDate.now());
        }

        clientRepo.save(client);
        logger.info("Client [{}] deactivated by {}", clientId, updatedByUsername);
    }

    public void deleteClient(String clientId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String updatedByUsername = auth.getName();

        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        client.setActive(false);
        client.setDeleted(true); // 👈 explicitly mark as deleted
        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedByUsername);

        for (RedirectUri uri : client.getRedirectUris()) {
            uri.setIsActive(false);
            uri.setIsDeleted(true);
            uri.setUpdatedBy(updatedByUsername);
            uri.setUpdatedAt(LocalDate.now());
        }

        for (ClientScope scope : client.getScopes()) {
            scope.setIsActive(false);
            scope.setIsDeleted(true);
            scope.setUpdatedBy(updatedByUsername);
            scope.setUpdatedAt(LocalDate.now());
        }

        clientRepo.save(client);
        logger.info("Client [{}] marked as deleted by {}", clientId, updatedByUsername);
    }

    @Transactional
    public void resetClientSecret(String clientId, @NotBlank String newSecret, String updatedBy) {
        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        client.setClientSecret(passwordEncoder.encode(newSecret));
        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedBy);

        clientRepo.save(client);
        logger.info("Client [{}] secret reset by {}", clientId, updatedBy);
    }

    public Optional<OAuthClient> getClientByClientId(String clientId) {
        return clientRepo.findByClientId(clientId)
                .filter(client -> !Boolean.TRUE.equals(client.getDeleted()));
    }

    // ===============================
    // PRIVATE HELPERS
    // ===============================

//    private Set<RedirectUri> mergeRedirectUris(OAuthClient existing, List<RedirectUri> updatedUris, String updatedBy) {
//        Map<String, RedirectUri> existingMap = existing.getRedirectUris().stream()
//                .filter(uri -> !Boolean.TRUE.equals(uri.getIsDeleted()))
//                .collect(Collectors.toMap(RedirectUri::getId, Function.identity()));
//
//        Set<RedirectUri> result = new HashSet<>();
//        for (RedirectUri updated : updatedUris) {
//            if (updated.getId() != null && existingMap.containsKey(updated.getId())) {
//                RedirectUri existingUri = existingMap.get(updated.getId());
//                existingUri.setUri(updated.getUri());
//                existingUri.setUpdatedAt(LocalDate.now());
//                existingUri.setUpdatedBy(updatedBy);
//                result.add(existingUri);
//            } else {
//                updated.setId(UUID.randomUUID().toString());
//                updated.setClient(existing);
//                updated.setCreatedAt(LocalDate.now());
//                updated.setCreatedBy(existing.getCreatedBy());
//                updated.setIsActive(true);
//                updated.setIsDeleted(false);
//                updated.setUpdatedAt(LocalDate.now());
//                updated.setUpdatedBy(updatedBy);
//                result.add(updated);
//            }
//        }
//        return result;
//    }

//    private Set<ClientScope> mergeScopes(OAuthClient existing, List<ClientScope> updatedScopes, String updatedBy) {
//        Map<String, ClientScope> existingMap = existing.getScopes().stream()
//                .filter(scope -> !Boolean.TRUE.equals(scope.getIsDeleted()))
//                .collect(Collectors.toMap(ClientScope::getId, Function.identity()));
//
//        Set<String> updatedIds = updatedScopes.stream()
//                .map(ClientScope::getId)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toSet());
//
//        Set<ClientScope> result = new HashSet<>();
//        for (ClientScope updated : updatedScopes) {
//            if (updated.getId() != null && existingMap.containsKey(updated.getId())) {
//                ClientScope existingScope = existingMap.get(updated.getId());
//                existingScope.setScope(updated.getScope());
//                existingScope.setUpdatedAt(LocalDate.now());
//                existingScope.setUpdatedBy(updatedBy);
//                result.add(existingScope);
//            } else {
//                updated.setClient(existing);
//                updated.setCreatedAt(LocalDate.now());
//                updated.setCreatedBy(existing.getCreatedBy());
//                updated.setIsActive(true);
//                updated.setIsDeleted(false);
//                updated.setUpdatedAt(LocalDate.now());
//                updated.setUpdatedBy(updatedBy);
//                result.add(updated);
//            }
//        }
//
//        // Mark removed scopes as deleted
//        for (ClientScope scope : existing.getScopes()) {
//            if (scope.getId() != null && !updatedIds.contains(scope.getId())) {
//                scope.setIsActive(false);
//                scope.setIsDeleted(true);
//                scope.setUpdatedAt(LocalDate.now());
//                scope.setUpdatedBy(updatedBy);
//                result.add(scope);
//            }
//        }
//
//        return result;
//    }
}
