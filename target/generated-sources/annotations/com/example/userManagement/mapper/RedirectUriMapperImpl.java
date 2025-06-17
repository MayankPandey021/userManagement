package com.example.userManagement.mapper;

import com.example.userManagement.dto.redirectUri.RedirectUriResponse;
import com.example.userManagement.entity.RedirectUri;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T15:40:04+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class RedirectUriMapperImpl implements RedirectUriMapper {

    @Override
    public RedirectUriResponse toDto(RedirectUri entity) {
        if ( entity == null ) {
            return null;
        }

        RedirectUriResponse redirectUriResponse = new RedirectUriResponse();

        redirectUriResponse.setId( entity.getId() );
        redirectUriResponse.setUri( entity.getUri() );
        redirectUriResponse.setIsActive( entity.getIsActive() );
        redirectUriResponse.setIsDeleted( entity.getIsDeleted() );
        redirectUriResponse.setCreatedAt( entity.getCreatedAt() );
        redirectUriResponse.setUpdatedAt( entity.getUpdatedAt() );
        redirectUriResponse.setCreatedBy( entity.getCreatedBy() );

        return redirectUriResponse;
    }

    @Override
    public RedirectUri toEntity(RedirectUriResponse dto) {
        if ( dto == null ) {
            return null;
        }

        RedirectUri redirectUri = new RedirectUri();

        redirectUri.setId( dto.getId() );
        redirectUri.setUri( dto.getUri() );
        redirectUri.setCreatedBy( dto.getCreatedBy() );
        redirectUri.setIsDeleted( dto.getIsDeleted() );
        redirectUri.setIsActive( dto.getIsActive() );
        redirectUri.setCreatedAt( dto.getCreatedAt() );
        redirectUri.setUpdatedAt( dto.getUpdatedAt() );

        return redirectUri;
    }
}
