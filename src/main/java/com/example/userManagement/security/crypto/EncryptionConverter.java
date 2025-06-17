package com.example.userManagement.security.crypto;

import com.example.userManagement.config.EncryptionProperties;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Converter
public class EncryptionConverter implements AttributeConverter<String, String> {



    private SecretKeySpec getKey() {
        return new SecretKeySpec(EncryptionProperties.getSecretKey().getBytes(), "AES");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            Cipher encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, getKey());
            byte[] encryptedBytes = encryptCipher.doFinal(attribute.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Cipher decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE, getKey());
            byte[] decryptedBytes = decryptCipher.doFinal(Base64.getDecoder().decode(dbData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}


