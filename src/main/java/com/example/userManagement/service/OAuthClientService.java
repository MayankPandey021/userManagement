package com.example.userManagement.service;

import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.repository.OAuthClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class OAuthClientService {
    private final OAuthClientRepository repo;

    public OAuthClientService(OAuthClientRepository repo) {
        this.repo = repo;
    }

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
        repo.save(client);
    }
}