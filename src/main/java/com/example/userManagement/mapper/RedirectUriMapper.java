package com.example.userManagement.mapper;

import com.example.userManagement.dto.redirectUri.RedirectUriResponse;
import com.example.userManagement.entity.RedirectUri;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RedirectUriMapper {
    RedirectUriResponse toDto(RedirectUri entity);
    RedirectUri toEntity(RedirectUriResponse dto);
}