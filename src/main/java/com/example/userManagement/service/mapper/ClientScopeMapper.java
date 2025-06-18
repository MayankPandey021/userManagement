package com.example.userManagement.service.mapper;

import com.example.userManagement.dto.scopes.CreateScopeRequest;
import com.example.userManagement.dto.scopes.ScopeResponse;
import com.example.userManagement.entity.ClientScope;
import com.example.userManagement.entity.OAuthClient;
import org.mapstruct.Mapper;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface ClientScopeMapper {

    default ClientScope toEntity(CreateScopeRequest request, OAuthClient client) {
        ClientScope scope = new ClientScope();
        scope.setClient(client);
        scope.setScope(request.getScope());
        scope.setIsActive(true);
        scope.setIsDeleted(false);
        scope.setCreatedAt(LocalDate.now());
        return scope;
    }

    default ScopeResponse toDto(ClientScope entity) {
        ScopeResponse resp = new ScopeResponse();
        resp.setClientId(entity.getClient().getClientId());
        resp.setScope(entity.getScope());
        return resp;
    }
}
