package com.dipanshu.BookManagerApi.security;

import com.dipanshu.BookManagerApi.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Base64;

@Component
public class TokenHashUtil {

    public String hash(String token) {
        try {
            return Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(token.getBytes())
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException( "Hashing failed");
        }
    }
}