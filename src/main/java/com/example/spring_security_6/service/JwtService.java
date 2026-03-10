package com.example.spring_security_6.service;

import com.example.spring_security_6.entiry.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Service
public class JwtService {

    private String secretKey = null;
    public String generateToken(User user) {
        Map<String, Object>  claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUserName())
                .issuer("DCB")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60*10*1000))
                .and()
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey(){
        byte[] decode = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);
    }


    public String getSecretKey(){
        return  secretKey = "3aQGx3UKFF7YyshERX1TY3fwKUxRHhxYnGSvzw8EzNV";
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T>  T extractClaims(String token, Function<Claims, T> claimresolver) {
        Claims claims = extractClaims(token);
        return claimresolver.apply(claims);

    }

    private Claims extractClaims(String token){
      return   Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName= extractUserName(token);

        return (userName.equals(userDetails.getUsername()) && !isTokenExpiried(token));
    }

    private boolean isTokenExpiried(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
