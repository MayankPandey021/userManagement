package com.example.userManagement.security;

import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.repository.OAuthClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
    private final OAuthClientRepository repo;

    public JpaRegisteredClientRepository(OAuthClientRepository repo) {
        this.repo = repo;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        OAuthClient entity = new OAuthClient();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setRedirectUri(registeredClient.getRedirectUris().iterator().next());
        entity.setAuthorizationGrantTypes("authorization_code,refresh_token");
        // Optionally set other fields (scopes, etc.) if added to the entity
        repo.save(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        Optional<OAuthClient> client = repo.findById(id);
        return client.map(this::toRegisteredClient).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Optional<OAuthClient> client = repo.findByClientId(clientId);
        return client.map(this::toRegisteredClient).orElse(null);
    }

    private RegisteredClient toRegisteredClient(OAuthClient entity) {
        return RegisteredClient.withId(entity.getId())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .redirectUri(entity.getRedirectUri())
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .scope("read")
                .scope("write")
                .build();
    }
}