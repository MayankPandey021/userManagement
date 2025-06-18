package com.example.userManagement.service.Interface;
import com.example.userManagement.dto.client.CreateClientRequest;
import com.example.userManagement.dto.client.OAuthClientList;
import com.example.userManagement.dto.client.UpdateClientRequest;
import com.example.userManagement.entity.OAuthClient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;


import java.util.Optional;
import java.util.List;

public interface IOAuthClientService {

    void updateClient(String clientId, @Valid UpdateClientRequest request);
    void createClient(@Valid CreateClientRequest request, String createdByUsername);
    List<OAuthClientList> getClients();



    void deleteClient(String clientId);


    Optional<OAuthClientList> getClientByClientId(String clientId);
}