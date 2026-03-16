package com.example.springbootweb.springbootweb.services;

import com.example.springbootweb.springbootweb.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;

    private SecretKey getSecretKey(){
//        System.out.println("SECTRT KEY : " + JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserEntity userEntity){
        return Jwts.builder()
                .subject(userEntity.getId().toString())
                .claim("email" ,userEntity.getEmail())
//                .claim("roles" , Set.of("ADMIN","USER"))//assining both roles
                .claim("roles" , userEntity.getRoles().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*15))
                .signWith(getSecretKey()) //need SecretKey object only
                .compact();
        //NOTE : claim is key value pair and can be n times .
        // expiry at the isssuedAt time + 1 minute just later

    }

    public String generateRefreshToken(UserEntity userEntity){
        return Jwts.builder()
                .subject(userEntity.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L *60*60*24*30)) // 30 days refreshTokn valdiidty
                .signWith(getSecretKey())
                .compact();

    }

    public Long getUserIdFromToken(String token){
        Claims claims =Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());

    }
}
