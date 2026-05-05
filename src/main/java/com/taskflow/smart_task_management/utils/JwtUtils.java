package com.taskflow.smart_task_management.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private String SECRET_KEY = "Taf#AFOS+SE/*FS-SF7&322oPS-^SA1!";

    public SecretKey getSignKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, username);
    }


    public String  createToken(Map<String,Object> claims,String subject){
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60) ) //5 minutes
                .signWith(getSignKey())
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();


    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    public String extractClaims(String token,String claim){
        return extractAllClaims(token).get(claim,String.class);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
