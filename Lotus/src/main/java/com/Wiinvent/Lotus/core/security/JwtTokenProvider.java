package com.Wiinvent.Lotus.core.security;

import com.Wiinvent.Lotus.core.config.LotusProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenProvider(LotusProperties lotusProperties) {
        String secret = lotusProperties.getJwt().getSecret();
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            keyBytes = Decoders.BASE64.decode(
                    java.util.Base64.getEncoder().encodeToString(keyBytes));
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes.length >= 32 ? keyBytes : padKey(keyBytes));
        this.accessTokenExpirationMs = lotusProperties.getJwt().getAccessTokenExpirationMs();
        this.refreshTokenExpirationMs = lotusProperties.getJwt().getRefreshTokenExpirationMs();
    }

    private byte[] padKey(byte[] keyBytes) {
        byte[] padded = new byte[32];
        System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
        return padded;
    }

    public String generateAccessToken(UserPrincipal principal) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpirationMs);
        return Jwts.builder()
                .subject(String.valueOf(principal.getId()))
                .claim("phone", principal.getPhone())
                .claim("role", principal.getRole().name())
                .claim("type", "access")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(UserPrincipal principal) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpirationMs);
        return Jwts.builder()
                .subject(String.valueOf(principal.getId()))
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isAccessToken(Claims claims) {
        return "access".equals(claims.get("type", String.class));
    }

    public boolean isRefreshToken(Claims claims) {
        return "refresh".equals(claims.get("type", String.class));
    }

    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }
}
