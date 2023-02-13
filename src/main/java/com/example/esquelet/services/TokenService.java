package com.example.esquelet.services;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    public String createToken(String email) {
        return Jwts.builder()
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(jwtSecret.getBytes()))
                .compact();
    }

    public String createValidationToken(String email) {
        return Jwts.builder()
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(jwtSecret.getBytes()))
                .compact();
    }

    public int validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(this.jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return 0;
        } catch (ExpiredJwtException e) {
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    public Claims getClaims(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String token = auth.substring(7);
        return getClaims(token);
    }

    public Claims getClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }
}
