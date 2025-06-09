package com.example.userManagement.repository;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.repository.OAuthClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDate;

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
        entity.setAuthorizationGrantTypes("authorization_code,refresh_token");

        // Set audit and status fields
        entity.setCreatedAt(LocalDate.now());
        entity.setCreatedBy("system"); // Replace with actual user if available
        entity.setActive(true);
        entity.setDeleted(false);

        // Handle redirect URIs
        Set<RedirectUri> redirectUris = registeredClient.getRedirectUris().stream()
                .map(uri -> {
                    RedirectUri redirectUri = new RedirectUri();
                    redirectUri.setUri(uri);
                    redirectUri.setClient(entity);
                    return redirectUri;
                })
                .collect(Collectors.toSet());
        entity.setRedirectUris(redirectUris);

        // Handle scopes
        Set<ClientScope> scopes = registeredClient.getScopes().stream()
                .map(scope -> {
                    ClientScope clientScope = new ClientScope();
                    clientScope.setScope(scope);
                    clientScope.setClient(entity);
                    return clientScope;
                })
                .collect(Collectors.toSet());
        entity.setScopes(scopes);

        repo.save(entity);
    }
    @Override
    public RegisteredClient findById(String id) {
        Optional<OAuthClient> client = repo.findById(id);
        return client.map(this::toRegisteredClient).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Optional<OAuthClient> client = repo.findByClientIdWithDetails(clientId);
        return client.map(this::toRegisteredClient).orElse(null);
    }

   private RegisteredClient toRegisteredClient(OAuthClient entity) {
       if (entity.getRedirectUris() == null || entity.getRedirectUris().isEmpty()) {
           throw new IllegalArgumentException("OAuthClient must have at least one redirect URI");
       }
         if (entity.getScopes() == null || entity.getScopes().isEmpty()) {
              throw new IllegalArgumentException("OAuthClient must have at least one scope");
         }
       RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId())
               .clientId(entity.getClientId())
               .clientSecret(entity.getClientSecret())
               .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
               .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN);

       // Add all redirect URIs
       if (entity.getRedirectUris() != null) {
           entity.getRedirectUris().forEach(redirectUri ->
               builder.redirectUri(redirectUri.getUri())
           );
       }

       // Add all scopes
       if (entity.getScopes() != null) {
           entity.getScopes().forEach(scope ->
               builder.scope(scope.getScope())
           );
       }

       return builder.build();
   }
}
