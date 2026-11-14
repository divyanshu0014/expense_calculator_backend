package com.basic.expensecalculatorbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    final SecretKey secretKey;

    public JwtService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(String fullName, Long userid) {
        return Jwts.builder()
                .subject("username")
                .claim("username", fullName)
                .claim("UserId", userid)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(secretKey)
                .compact();
    }


    public boolean validateToken(String token) {
            try{
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token);
                return true;
            } catch (JwtException | IllegalArgumentException e) {
                return false;
            }
    }


    public Claims claimToken (String token) {
        return Jwts
                 .parser()
                 .build()
                 .parseClaimsJws(token)
                 .getBody();
    }

}
