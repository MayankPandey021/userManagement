package com.example.userManagement.service.mapper;

import com.example.userManagement.dto.redirectUri.RedirectUriResponse;
import com.example.userManagement.dto.redirectUri.CreateRedirectUriRequest;
import com.example.userManagement.entity.OAuthClient;
import com.example.userManagement.entity.RedirectUri;
import org.mapstruct.Mapper;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface RedirectUriMapper {

    RedirectUriResponse toDto(RedirectUri entity);

    default RedirectUri toEntity(CreateRedirectUriRequest request, OAuthClient client, String createdBy) {
        RedirectUri uri = new RedirectUri();
        uri.setClient(client);
        uri.setUri(request.getUri());
        uri.setIsActive(true);
        uri.setIsDeleted(false);
        uri.setCreatedBy(createdBy);
        uri.setCreatedAt(LocalDate.now());
        return uri;
    }
}
