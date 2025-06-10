package com.example.userManagement.dto;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;


@Getter
@Setter
public class  OAuthClientCreateRequest{

    private String clientId;
    private String clientName;
    private Set<String> scopes;
}
