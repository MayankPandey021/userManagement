package com.example.userManagement.mapper;

import com.example.userManagement.entity.ClientScope;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ClientScopeMapper {

    // Entity → String
    default String toStringScope(ClientScope scope) {
        return scope.getScope();
    }

    // String → Entity
    default ClientScope toEntity(String scope) {
        ClientScope s = new ClientScope();
        s.setScope(scope);
        return s;
    }




}
