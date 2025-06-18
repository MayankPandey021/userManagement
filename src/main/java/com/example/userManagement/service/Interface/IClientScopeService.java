package com.example.userManagement.service.Interface;
import com.example.userManagement.dto.scopes.CreateScopeRequest;
import com.example.userManagement.dto.scopes.ScopeDetailResponse;
import com.example.userManagement.dto.scopes.ScopeResponse;
import com.example.userManagement.dto.scopes.UpdateScopeRequest;

import java.util.List;

public interface IClientScopeService {
    void createScope(CreateScopeRequest request);

    List<ScopeResponse> getScopes();

    List<ScopeDetailResponse> getScopesByClientId(String clientId);

    void updateScope(UpdateScopeRequest request);

    void deleteScope(String clientId);


}