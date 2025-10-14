package iuh.fit.se.iwork4se.security;

import iuh.fit.se.iwork4se.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties props;
    private SecretKey hmacKey;

    @PostConstruct
    void init() {
        // HS256: secret nên >= 64 bytes
        this.hmacKey = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UUID userId, String email, String phone, String role) {
        Instant now = Instant.now();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        if (email != null) claims.put("email", email);
        if (phone != null) claims.put("phone", phone);
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(props.getIssuer())
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(props.getAccessExpSeconds())))
                .claims(claims)
                .signWith(hmacKey)
                .compact();
    }

    public TokenWithId generateRefreshToken(UUID userId) {
        Instant now = Instant.now();
        String jti = UUID.randomUUID().toString();
        String token = Jwts.builder()
                .id(jti)
                .issuer(props.getIssuer())
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(props.getRefreshExpSeconds())))
                .signWith(hmacKey)
                .compact();
        return new TokenWithId(jti, token);
    }

    public Jws<Claims> parseAndValidate(String token) {
        // Parser API 0.13.x
        return Jwts.parser()
                .verifyWith(hmacKey)
                .clockSkewSeconds(props.getClockSkewSeconds())
                .requireIssuer(props.getIssuer())
                .build()
                .parseSignedClaims(token);
    }

    public record TokenWithId(String jti, String token) {}
}
