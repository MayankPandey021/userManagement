package com.example.userManagement.service.Interface;

import com.example.userManagement.dto.redirectUri.ClientRedirectUrisResponse;
import com.example.userManagement.dto.redirectUri.CreateRedirectUriRequest;
import com.example.userManagement.dto.redirectUri.RedirectUriResponse;
import com.example.userManagement.entity.RedirectUri;

import java.util.List;

public interface IRedirectUriService {

    RedirectUriResponse create(CreateRedirectUriRequest request);
    void delete(String clientId, String uri, String updatedBy);
    void update(String clientId, String oldUri, String newUri, String updatedBy);
    List<RedirectUri> getById(String clientId);
    List<ClientRedirectUrisResponse> get();
}