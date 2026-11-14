package com.basic.expensecalculatorbackend.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey getSecretKey(@Value("${jwt.secret}") String encodedKey) {

        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
        return Keys.hmacShaKeyFor(keyBytes) ;
    }

}
