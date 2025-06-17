package com.example.userManagement.service.abstraction;

import com.example.userManagement.dto.redirectUri.ClientRedirectUrisResponse;
import com.example.userManagement.dto.redirectUri.CreateRedirectUriRequest;
import com.example.userManagement.dto.redirectUri.RedirectUriResponse;
import com.example.userManagement.entity.RedirectUri;

import java.util.List;

public interface IRedirectUriService {

    RedirectUriResponse createRedirectUri(CreateRedirectUriRequest request);

    void deleteRedirectUri(String clientId, String uri, String updatedBy);
    void updateRedirectUri(String clientId, String oldUri, String newUri, String updatedBy);

    List<RedirectUri> getRedirectUrisByClientId(String clientId);

    List<ClientRedirectUrisResponse> getAllActiveRedirectUrisByClientId();
}