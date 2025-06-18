package com.example.userManagement.service.Implementation;

import com.example.userManagement.dto.redirectUri.ClientRedirectUrisResponse;
import com.example.userManagement.dto.redirectUri.CreateRedirectUriRequest;
import com.example.userManagement.dto.redirectUri.RedirectUriResponse;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import com.example.userManagement.exception.ClientNotFoundException;
import com.example.userManagement.service.mapper.RedirectUriMapper;
import com.example.userManagement.repository.OAuthClientRepository;
import com.example.userManagement.repository.RedirectUriRepository;
import com.example.userManagement.service.abstraction.IRedirectUriService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public RedirectUriResponse createRedirectUri(@Valid CreateRedirectUriRequest request) {
        String createdBy = SecurityContextHolder.getContext().getAuthentication().getName();

        OAuthClient client = clientRepo.findByClientId(request.getClientId())
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        RedirectUri uri = redirectUriMapper.toEntity(request, client, createdBy);
        redirectUriRepo.save(uri);

        return redirectUriMapper.toDto(uri);
    }

    public void deleteRedirectUri(String clientId, String uri, String updatedBy) {
        OAuthClient client = clientRepo.findByClientId(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        RedirectUri targetUri = client.getRedirectUris().stream()
                .filter(r -> r.getUri().equals(uri) && !Boolean.TRUE.equals(r.getIsDeleted()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Redirect URI not found"));

        markAsDeleted(targetUri, updatedBy);
        redirectUriRepo.save(targetUri);
    }

    public void updateRedirectUri(String clientId, String oldUri, String newUri, String updatedBy) {
        RedirectUri uri = redirectUriRepo.findByClient_ClientIdAndUriAndIsDeletedFalse(clientId, oldUri)
                .orElseThrow(() -> new RuntimeException("Redirect URI not found for client"));

        updateUri(uri, newUri, updatedBy);
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

        return grouped.entrySet().stream()
                .map(entry -> mapToResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // --- Moved helper methods ---

    private void markAsDeleted(RedirectUri uri, String updatedBy) {
        uri.setIsDeleted(true);
        uri.setIsActive(false);
        uri.setUpdatedBy(updatedBy);
        uri.setUpdatedAt(LocalDate.now());
    }

    private void updateUri(RedirectUri uri, String newUri, String updatedBy) {
        uri.setUri(newUri);
        uri.setUpdatedBy(updatedBy);
        uri.setUpdatedAt(LocalDate.now());
    }

    private ClientRedirectUrisResponse mapToResponse(String clientId, List<String> uris) {
        ClientRedirectUrisResponse response = new ClientRedirectUrisResponse();
        response.setClientId(clientId);
        response.setRedirectUris(uris);
        return response;
    }
}
