package com.example.userManagement.service.Implementation;

import com.example.userManagement.dto.client.CreateClientRequest;
import com.example.userManagement.dto.client.OAuthClientList;
import com.example.userManagement.dto.client.UpdateClientRequest;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.exception.ClientNotFoundException;
import com.example.userManagement.mapper.OAuthClientMapper;
import com.example.userManagement.repository.OAuthClientRepository;
import com.example.userManagement.service.abstraction.IOAuthClientService;

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
import java.util.stream.Collectors;

@Service
public class OAuthClientService implements IOAuthClientService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthClientService.class);

    @Autowired
    private OAuthClientRepository clientRepo;

    @Autowired
    private com.example.userManagement.service.implementation.UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OAuthClientMapper mapper;


    // In OAuthClientService.java

    @Transactional
    public void updateClient(String clientId, @Valid UpdateClientRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String updatedByUsername = auth.getName();

        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        // Patch update: authorizationGrantTypes
        if (request.getAuthorizationGrantTypes() != null) {
            client.setAuthorizationGrantTypes(String.valueOf(request.getAuthorizationGrantTypes()));
        }

        // Patch update: isActive
        if (request.getIsActive() != null) {
            client.setActive(request.getIsActive());
            // Propagate isActive to related entities
            client.getRedirectUris().forEach(uri -> {
                uri.setIsActive(request.getIsActive());
                uri.setUpdatedBy(updatedByUsername);
                uri.setUpdatedAt(LocalDate.now());
            });
            client.getScopes().forEach(scope -> {
                scope.setIsActive(request.getIsActive());
                scope.setUpdatedBy(updatedByUsername);
                scope.setUpdatedAt(LocalDate.now());
            });
        }

        // Patch update: clientSecret
        if (request.getClientSecret() != null) {
            client.setClientSecret(passwordEncoder.encode(request.getClientSecret()));
        }

        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedByUsername);

        clientRepo.save(client);
    }

    public void createClient(@Valid CreateClientRequest request, String createdByUsername) {
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

        if (request.getRedirectUris() != null) {
            client.setRedirectUris(mapper.mapRedirectUris(request.getRedirectUris(), client, createdByUsername));
        }

        if (request.getScopes() != null) {
            client.setScopes(mapper.mapScopes(request.getScopes(), client, createdByUsername));
        }

        clientRepo.save(client);
        logger.info("Client [{}] created by {}", client.getClientId(), createdByUsername);
    }

    public List<OAuthClientList> getClients() {
        return clientRepo.findAll().stream()
                .filter(client -> Boolean.TRUE.equals(client.getActive()) && Boolean.FALSE.equals(client.getDeleted()))
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }



    public void deleteClient(String clientId) {
        String updatedByUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        client.setActive(false);
        client.setDeleted(true);
        client.setUpdatedAt(LocalDate.now());
        client.setUpdatedBy(updatedByUsername);

        client.getRedirectUris().forEach(uri -> {
            uri.setIsActive(false);
            uri.setIsDeleted(true);
            uri.setUpdatedBy(updatedByUsername);
            uri.setUpdatedAt(LocalDate.now());
        });

        client.getScopes().forEach(scope -> {
            scope.setIsActive(false);
            scope.setIsDeleted(true);
            scope.setUpdatedBy(updatedByUsername);
            scope.setUpdatedAt(LocalDate.now());
        });

        clientRepo.save(client);
        logger.info("Client [{}] marked as deleted by {}", clientId, updatedByUsername);
    }


    public Optional<OAuthClient> getClientByClientId(String clientId) {
        return clientRepo.findByClientId(clientId)
                .filter(client -> !Boolean.TRUE.equals(client.getDeleted()));
    }
}


