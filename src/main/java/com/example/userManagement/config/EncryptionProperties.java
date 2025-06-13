package com.example.userManagement.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionProperties {

    @Getter
    private static String secretKey;

    public EncryptionProperties(@Value("${ENCRYPTION_SECRET}") String key) {
        EncryptionProperties.secretKey = key;
    }

}