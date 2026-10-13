package com.dedea.backend.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(@Value("${app.jwt.secreto}") String secreto,
                      @Value("${app.jwt.expiracion-ms}") long expiracionMs) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMs;
    }

    public String generarToken(Long usuarioId) {
        return Jwts.builder()
                .subject(usuarioId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiracionMs))
                .signWith(clave)
                .compact();
    }

    public Long extraerId(String token) {
        return Long.valueOf(Jwts.parser().verifyWith(clave).build()
                .parseSignedClaims(token).getPayload().getSubject());
    }

    //Este método revisa la firma y el vencimiento, si alguien cambió el token o ya venció no es válido
    public boolean esValido(String token) {
        try {
            Jwts.parser().verifyWith(clave).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}