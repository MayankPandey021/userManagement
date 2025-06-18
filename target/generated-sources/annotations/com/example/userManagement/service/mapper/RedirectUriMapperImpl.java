package com.example.userManagement.service.mapper;

import com.example.userManagement.dto.redirectUri.RedirectUriResponse;
import com.example.userManagement.entity.RedirectUri;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-18T18:18:16+0530",
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
}
