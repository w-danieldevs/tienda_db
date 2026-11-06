package com.tienda.demo.service;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.tienda.demo.entity.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;

@Service 
public class JwtService {
     @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    // Obtener la clave secreta
    private SecretKey getKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generar JWT
    public String generateToken(Users user) {

        Date now = new Date();

        Date expirationDate =
                new Date(now.getTime() + expiration);

        return Jwts.builder()

                .subject(user.getUsername())

                .claim("userId", user.getId())

                .claim("rolId", user.getRole().getId())

                .claim("role", user.getRole().getName().name())

                .issuedAt(now)

                .expiration(expirationDate)

                .signWith(getKey())

                .compact();
    }

    // Leer claims del token
    public Claims getClaims(String token) {

        return Jwts.parser()

                .verifyWith(getKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }

    // Validar token
    public boolean validateToken(String token) {

        try {

            getClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    // Obtener username
    public String getUsername(String token) {

        return getClaims(token).getSubject();
    }

    // Obtener userId
    public Long getUserId(String token) {

        return getClaims(token).get("userId", Long.class);
    }

    // Obtener rolId
    public Long getRolId(String token) {

        return getClaims(token).get("rolId", Long.class);
    }

    // Obtener el nombre del rol
public String getRole(String token) {
    return getClaims(token).get("role", String.class);
}

    // Renovar token
    public String refreshToken(String token) {

        Claims claims = getClaims(token);

        String username = claims.getSubject();

        String role = claims.get("role", String.class);

        Long userId = claims.get("userId", Long.class);

        Long rolId = claims.get("rolId", Long.class);

        Date now = new Date();

        Date expirationDate =
                new Date(now.getTime() + expiration);

        return Jwts.builder()

                .subject(username)

                .claim("userId", userId)

                .claim("rolId", rolId)

                .claim("role", role)

                .issuedAt(now)

                .expiration(expirationDate)

                .signWith(getKey())

                .compact();
    }
}
