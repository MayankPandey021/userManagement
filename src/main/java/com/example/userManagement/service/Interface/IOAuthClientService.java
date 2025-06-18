package com.example.userManagement.service.Interface;
import com.example.userManagement.dto.client.CreateClientRequest;
import com.example.userManagement.dto.client.OAuthClientList;
import com.example.userManagement.dto.client.UpdateClientRequest;
import com.example.userManagement.entity.OAuthClient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

public interface IOAuthClientService {

    void updateClient(String clientId, @Valid UpdateClientRequest request);
    void createClient(@Valid CreateClientRequest request, String createdByUsername);
    List<OAuthClientList> getClients();



    void deleteClient(String clientId);


    Optional<OAuthClient> getClientByClientId(String clientId);
}