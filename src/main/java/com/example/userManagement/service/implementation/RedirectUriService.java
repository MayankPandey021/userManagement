// src/main/java/com/example/userManagement/service/implementation/RedirectUriService.java
package com.example.userManagement.service.implementation;

import com.example.userManagement.dto.redirectUri.ClientRedirectUrisResponse;
import com.example.userManagement.dto.redirectUri.RedirectUriResponse;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.exception.ClientNotFoundException;
import com.example.userManagement.mapper.RedirectUriMapper;
import com.example.userManagement.repository.OAuthClientRepository;
import com.example.userManagement.repository.RedirectUriRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.userManagement.service.abstraction.IRedirectUriService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedirectUriService implements IRedirectUriService {

    private final OAuthClientRepository clientRepo;
    private final RedirectUriRepository redirectUriRepo;
    private final RedirectUriMapper redirectUriMapper;
    private final Logger logger = LoggerFactory.getLogger(RedirectUriService.class);

    public RedirectUriResponse createRedirectUri(com.example.userManagement.dto.redirectUri.@Valid CreateRedirectUriRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String createdBy = auth.getName();

        OAuthClient client = clientRepo.findByClientId(request.getClientId())
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        RedirectUri uri = new RedirectUri();
        uri.setClient(client);
        uri.setUri(request.getUri());
        uri.setIsActive(true);
        uri.setIsDeleted(false);
        uri.setCreatedBy(createdBy);
        uri.setCreatedAt(LocalDate.now());

        redirectUriRepo.save(uri);

        return redirectUriMapper.toDto(uri);
    }

    public void deleteRedirectUri(String clientId, String uri, String updatedBy) {
        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        RedirectUri targetUri = client.getRedirectUris().stream()
                .filter(r -> r.getUri().equals(uri) && !Boolean.TRUE.equals(r.getIsDeleted()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Redirect URI not found"));

        targetUri.setIsDeleted(true);
        targetUri.setIsActive(false);
        targetUri.setUpdatedBy(updatedBy);
        targetUri.setUpdatedAt(LocalDate.now());

        redirectUriRepo.save(targetUri);
    }

    public void updateRedirectUri(String clientId, String oldUri, String newUri, String updatedBy) {
        RedirectUri uri = redirectUriRepo.findByClient_ClientIdAndUriAndIsDeletedFalse(clientId, oldUri)
                .orElseThrow(() -> new RuntimeException("Redirect URI not found for client"));

        uri.setUri(newUri);
        uri.setUpdatedBy(updatedBy);
        uri.setUpdatedAt(LocalDate.now());

        redirectUriRepo.save(uri);
    }

    public List<RedirectUri> getRedirectUrisByClientId(String clientId) {
        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));
        return client.getRedirectUris().stream()
                .filter(uri -> !Boolean.TRUE.equals(uri.getIsDeleted()))
                .toList();
    }

    public List<ClientRedirectUrisResponse> getAllActiveRedirectUrisByClientId() {
        List<RedirectUri> allUris = redirectUriRepo.findAllByIsDeletedFalseAndIsActiveTrue();

        Map<String, List<String>> grouped = allUris.stream()
                .collect(Collectors.groupingBy(
                        uri -> uri.getClient().getClientId(),
                        Collectors.mapping(RedirectUri::getUri, Collectors.toList())
                ));

        return grouped.entrySet().stream().map(entry -> {
            ClientRedirectUrisResponse res = new ClientRedirectUrisResponse();
            res.setClientId(entry.getKey());
            res.setRedirectUris(entry.getValue());
            return res;
        }).collect(Collectors.toList());
    }
}