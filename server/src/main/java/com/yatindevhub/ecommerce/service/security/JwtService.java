package com.yatindevhub.ecommerce.service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${secret}")
    private  String SECRET;

    private String expectedIssuer = "com.yatindevhub.ecommerce";
    private String expectedAudience = "ecommerce-client";
    private final UserServiceDetailsImpl userServiceDetails;
    public String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public String GenerateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        UserDetails userDetails = userServiceDetails.loadUserByUsername(username);
        claims.put("roles", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return createToken(claims, username);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public String createToken(Map<String, Object> claims,String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer(expectedIssuer)
                .setAudience(expectedAudience)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*15))

                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    public boolean validateToken(String token, UserDetails userDetails){
        try{
            Claims claims = extractAllClaims(token);
            String subject = claims.getSubject();
            if (!subject.equals(userDetails.getUsername())) return false;

            // issuer & audience checks
            if (!expectedIssuer.equals(claims.getIssuer())) return false;
            String aud = claims.getAudience().iterator().next();
            if (!"ecommerce-client".equals(aud)) {
                throw new JwtException("Invalid audience");
            }
            if (aud == null || !expectedAudience.equals(aud)) return false;

            // expiration handled by parseClaims; you can also check
            return true;
//            final String username = extractUserName(token);
//            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }catch (ExpiredJwtException e){
                throw e;
        }catch (JwtException ex){
                return false;
        }

    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
