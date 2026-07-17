package com.kalyani.car_rental_backend.config;

import com.kalyani.car_rental_backend.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String jwtSecret;
    @Value("${jwt.expiration}") private long jwtExpiration;

    public String generateToken(User user) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        claims.put("email",user.getEmail());
        claims.put("fullName",user.getFullName());
        claims.put("role",user.getRole().name());
        return Jwts.builder().claims(claims).subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(getSigningKey()).compact();
    }

    public String extractUsername(String token){return extractClaim(token,Claims::getSubject);}
    public boolean isTokenValid(String token,String username){
        return username.equals(extractUsername(token)) && !extractClaim(token,Claims::getExpiration).before(new Date());
    }
    private <T> T extractClaim(String token,Function<Claims,T> resolver){
        Claims claims=Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        return resolver.apply(claims);
    }
    private SecretKey getSigningKey(){
        try{return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));}
        catch(Exception ignored){return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));}
    }
}
