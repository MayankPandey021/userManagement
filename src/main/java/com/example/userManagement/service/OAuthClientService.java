package com.example.userManagement.service;

import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.repository.OAuthClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OAuthClientService {

    @Autowired
    private OAuthClientRepository clientRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //to make it consistent with redirectUri and clientScope.
    @Transactional
    public void updateClientStatus(OAuthClient client, boolean isDeleted, boolean isActive, String updatedBy) {
        client.setDeleted(isDeleted);
        client.setActive(isActive);
        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedBy);

        if (client.getRedirectUris() != null) {
            for (RedirectUri uri : client.getRedirectUris()) {
                uri.setDeleted(isDeleted);
                uri.setActive(isActive);
                uri.setUpdatedAt(client.getUpdatedAt());
                uri.setUpdatedBy(updatedBy);
            }
        }
        if (client.getScopes() != null) {
            for (ClientScope scope : client.getScopes()) {
                scope.setDeleted(isDeleted);
                scope.setActive(isActive);
                scope.setUpdatedAt(client.getUpdatedAt());
                scope.setUpdatedBy(updatedBy);
            }
        }
        clientRepo.save(client);
    }



    public OAuthClient createClient(OAuthClient client, String createdByUsername) {
        if (clientRepo.existsByClientId(client.getClientId())) {
            throw new IllegalArgumentException("ClientId already exists");
        }
        client.setCreatedAt(LocalDate.now());
        client.setCreatedBy(createdByUsername);
        client.setActive(true);
        client.setDeleted(false);
        return clientRepo.save(client);
    }

    public OAuthClient updateClient(String clientId, OAuthClient updated, String updatedByUsername) {
        OAuthClient existing = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        existing.setClientSecret(updated.getClientSecret());
        existing.setAuthorizationGrantTypes(updated.getAuthorizationGrantTypes());
        existing.setScopes(updated.getScopes());
        existing.setRedirectUris(updated.getRedirectUris());
        existing.setUpdatedAt(LocalDate.now());
        existing.setUpdatedBy(updatedByUsername);
        return clientRepo.save(existing);
    }

    public List<OAuthClient> listActiveClients() {
        return clientRepo.findAllByIsDeletedFalseAndIsActiveTrue();
    }

    public Optional<OAuthClient> getClientByClientId(String clientId) {
        return clientRepo.findByClientIdAndIsDeletedFalse(clientId);
    }

    public void resetClientSecret(String clientId, String newSecret, String updatedByUsername) {
        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        client.setClientSecret(passwordEncoder.encode(newSecret));
        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedByUsername);
        clientRepo.save(client);
    }

    public void deactivateClient(String clientId, String updatedByUsername) {
        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        client.setActive(false);
        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedByUsername);
        clientRepo.save(client);
    }

    public void deleteClient(String clientId, String updatedByUsername) {
        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        client.setDeleted(true);
        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedByUsername);
        clientRepo.save(client);
    }
}