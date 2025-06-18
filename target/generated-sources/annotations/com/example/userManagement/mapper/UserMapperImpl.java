package com.example.userManagement.mapper;

import com.example.userManagement.dto.user.UserCreateRequest;
import com.example.userManagement.dto.user.UserResponseDto;
import com.example.userManagement.dto.user.UserUpdateRequest;
import com.example.userManagement.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-18T11:58:14+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.7 (Homebrew)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserCreateRequest dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( dto.getFirstName() );
        user.setMiddleName( dto.getMiddleName() );
        user.setLastName( dto.getLastName() );
        user.setEmail( dto.getEmail() );
        user.setMobile( dto.getMobile() );
        user.setUsername( dto.getUsername() );
        user.setPassword( dto.getPassword() );

        user.setIsActive( true );
        user.setIsDeleted( false );

        return user;
    }

    @Override
    public UserResponseDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.isActive = user.isActive();
        userResponseDto.isDeleted = user.isDeleted();
        userResponseDto.id = user.getId();
        userResponseDto.firstName = user.getFirstName();
        userResponseDto.lastName = user.getLastName();
        userResponseDto.username = user.getUsername();
        userResponseDto.createdBy = user.getCreatedBy();

        return userResponseDto;
    }

    @Override
    public void updateUserFromDto(UserUpdateRequest dto, User user) {
        if ( dto == null ) {
            return;
        }

        if ( dto.firstName != null ) {
            user.setFirstName( dto.firstName );
        }
        if ( dto.middleName != null ) {
            user.setMiddleName( dto.middleName );
        }
        if ( dto.lastName != null ) {
            user.setLastName( dto.lastName );
        }
        if ( dto.email != null ) {
            user.setEmail( dto.email );
        }
        if ( dto.mobile != null ) {
            user.setMobile( dto.mobile );
        }
        if ( dto.username != null ) {
            user.setUsername( dto.username );
        }
        if ( dto.password != null ) {
            user.setPassword( dto.password );
        }
        user.setIsActive( dto.isActive );
    }
}
