package com.example.userManagement.service.Interface;
import com.example.userManagement.dto.scopes.CreateScopeRequest;
import com.example.userManagement.dto.scopes.ScopeDetailResponse;
import com.example.userManagement.dto.scopes.ScopeResponse;
import com.example.userManagement.dto.scopes.UpdateScopeRequest;

import java.util.List;

public interface IClientScopeService {
    void create(CreateScopeRequest request);
    List<ScopeResponse> get();
    List<ScopeDetailResponse> getById(String clientId);
    void update(UpdateScopeRequest request);
    void delete(String clientId);


}