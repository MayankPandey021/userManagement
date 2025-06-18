package com.example.userManagement.service.Interface;
import com.example.userManagement.dto.client.CreateClientRequest;
import com.example.userManagement.dto.client.OAuthClientList;
import com.example.userManagement.dto.client.UpdateClientRequest;

import jakarta.validation.Valid;


import java.util.Optional;
import java.util.List;

public interface IOAuthClientService {
    void update(String clientId, @Valid UpdateClientRequest request);
    void create(@Valid CreateClientRequest request, String createdByUsername);
    List<OAuthClientList> get();
    void delete(String clientId);
    Optional<OAuthClientList> getById(String clientId);
}