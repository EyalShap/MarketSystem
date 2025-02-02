package com.sadna.sadnamarket.domain.auth;
import java.sql.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenService { 

@Value("${jwt.secret}")
private String secret; 
private final long expirationTime = 1000 * 60 * 60 * 24;
 // 24 hours 
private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); 
    private static final Logger logger = LogManager.getLogger(AuthFacade.class);

public String generateToken(String username) {
     logger.info("start-generateToken. args: "+username);
     return Jwts.builder()
     .setSubject(username)
     .setIssuedAt(new Date(System.currentTimeMillis()))
     .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) 
     .signWith(key) 
     .compact(); 
} 

public boolean validateToken(String token) {
     logger.info("start-validateToken. args: "+token);
     try { 
        Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);
         return true; }
          catch (Exception e) {
             return false; } } 


public String extractUsername(String token) { 
    return extractClaim(token, Claims::getSubject); 
} 
public Date extractExpiration(String token) {
     return (Date) extractClaim(token, Claims::getExpiration); 
} 
public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { 
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims); 
} 
private Claims extractAllClaims(String token) {
     return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); } 

}
