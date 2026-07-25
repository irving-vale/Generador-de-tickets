package com.joirv.CursoSpringBoot.infraestructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    @Value( "${app.jwt.secret}")
    private String secretKey;
    @Value( "${app.jwt.expiration}")
    private int jwtExpirationMs;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        // Extraemos los roles y los convertimos a una cadena separada por comas (ej. "read,write")
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        extraClaims.put("authorities", roles);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername()) // Guardamos el email/usuario
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSignInKey()) // Firmamos con la clave secreta
                .compact();
    }

    // 3. EXTRAER EL USUARIO (EMAIL) DEL TOKEN
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 4. EXTRAER LOS ROLES DEL TOKEN
    public String extractAuthorities(String token) {
        return extractClaim(token, claims -> claims.get("authorities", String.class));
    }

    // 5. VALIDAR EL TOKEN
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // --- MÉTODOS PRIVADOS AUXILIARES ---

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey()) // Revisa matemáticamente que la firma sea real
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        // Convierte nuestra clave Base64 de las propiedades a una SecretKey criptográfica
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
