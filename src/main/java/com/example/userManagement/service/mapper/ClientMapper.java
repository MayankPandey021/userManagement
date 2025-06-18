package com.example.userManagement.service.mapper;

import com.example.userManagement.dto.client.CreateClientRequest;
import com.example.userManagement.dto.client.OAuthClientList;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ClientMapper {

    private final PasswordEncoder passwordEncoder;

    public ClientMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public OAuthClientList toDto(OAuthClient client) {
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

    public OAuthClient toEntity(CreateClientRequest request, String createdBy) {
        OAuthClient client = new OAuthClient();
        client.setClientId(request.getClientId());
        client.setClientSecret(passwordEncoder.encode(request.getClientSecret()));
        client.setAuthorizationGrantTypes(String.join(",", request.getAuthorizationGrantTypes()));
        client.setCreatedAt(LocalDate.now());
        client.setCreatedBy(createdBy);
        client.setActive(true);
        client.setDeleted(false);
        return client;
    }
}
