package com.example.userManagement.mapper;

import com.example.userManagement.dto.client.OAuthClientList;
import com.example.userManagement.dto.client.RedirectUriDto;
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



    public Set<RedirectUri> mapRedirectUris(List<RedirectUriDto> uris, OAuthClient client, String createdBy) {
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

    public OAuthClient toEntity(OAuthClientList dto, String createdBy) {
        OAuthClient client = new OAuthClient();
        client.setClientId(dto.getClientId());
        client.setClientSecret(passwordEncoder.encode(dto.getClientSecret()));
        client.setAuthorizationGrantTypes(
                String.join(",", dto.getAuthorizationGrantTypes())
        );
        client.setRedirectUris(
                mapRedirectUris(
                        dto.getRedirectUris().stream()
                                .map(uri -> {
                                    RedirectUriDto uriDto = new RedirectUriDto();
                                    uriDto.setUri(uri);
                                    return uriDto;
                                })
                                .collect(Collectors.toList()),
                        client,
                        createdBy
                )
        );
        client.setScopes(
                mapScopes(dto.getScopes(), client, createdBy)
        );

        return client;
    }
}
