package com.example.userManagement.mapper;

import com.example.userManagement.dto.client.CreateClientRequest;
import com.example.userManagement.dto.client.OAuthClientList;
import com.example.userManagement.dto.client.UpdateClientRequest;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OAuthClientMapper {

    private final PasswordEncoder passwordEncoder;

    public OAuthClientMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public OAuthClient mapCreateRequestToEntity(CreateClientRequest request, String createdBy) {
        OAuthClient client = new OAuthClient();
        client.setClientId(request.getClientId());
        client.setClientSecret(passwordEncoder.encode(request.getClientSecret()));
        client.setAuthorizationGrantTypes(String.valueOf(request.getAuthorizationGrantTypes()));
        client.setCreatedAt(LocalDate.now());
        client.setCreatedBy(createdBy);
        client.setActive(true);
        client.setDeleted(false);

        client.setRedirectUris(mapRedirectUris(request.getRedirectUris(), client, createdBy));
        client.setScopes(mapScopes(request.getScopes(), client, createdBy));

        return client;
    }

    public void mapUpdateRequestToEntity(OAuthClient client, UpdateClientRequest request, String updatedBy) {
        if (request.getClientSecret() != null) {
            client.setClientSecret(passwordEncoder.encode(request.getClientSecret()));
        }

        if (request.getAuthorizationGrantTypes() != null) {
            client.setAuthorizationGrantTypes(String.valueOf(request.getAuthorizationGrantTypes()));
        }

        if (request.getRedirectUris() != null) {
            client.getRedirectUris().clear();
            client.getRedirectUris().addAll(mapRedirectUris(request.getRedirectUris(), client, updatedBy));
        }

        if (request.getScopes() != null) {
            client.getScopes().clear();
            client.getScopes().addAll(mapScopes(request.getScopes(), client, updatedBy));
        }

        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedBy);
    }

    public Set<RedirectUri> mapRedirectUris(List<String> uris, OAuthClient client, String createdBy) {
        if (uris == null) return Set.of();
        return uris.stream().map(uriStr -> {
            RedirectUri uri = new RedirectUri();
            uri.setId(UUID.randomUUID().toString());
            uri.setUri(uriStr);
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

    public Set<ClientScope> mapScopes(List<String> scopes, OAuthClient client, String createdBy) {
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


    public OAuthClientList mapToDto(OAuthClient client) {
        OAuthClientList dto = new OAuthClientList();

        dto.setClientId(client.getClientId());
        dto.setClientSecret(client.getClientSecret());
        dto.setAuthorizationGrantTypes(List.of(client.getAuthorizationGrantTypes()));

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
    }
}
