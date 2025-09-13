package com.taskmanager.gateway.util;

import com.taskmanager.gateway.exception.CustomSecurityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    @Value("${jwt.secretkey}")
    private String SECRET_KEY;

    private static final String JWT_ISSUER_SYSTEM = "System";
    private static final String SYSTEM_ACCESS_TOKEN_EXPIRED = "System Access Token Expired";
    private static final String ACCESS_TOKEN_EXPIRED = "Access token expired for user: %s";
    private static final String TOKEN_EXPIRED = "Access token expired";
    private static final String USERNAME = "username";
    private static final String BEARER = "Bearer";
    private static final String INVALID_TOKEN = "Token missing or invalid format.";

    private Key getKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims validateAuthorizationHeader(String authorizationHeader) {
        Claims claims = null;
        try {
            String token = extractBearerToken(authorizationHeader);
            claims = validateTokenExpiration(token);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomSecurityException(HttpStatus.UNAUTHORIZED, TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new CustomSecurityException(HttpStatus.UNAUTHORIZED, e);
        }
        return claims;
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            throw new CustomSecurityException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN);
        }
        return authorizationHeader.substring(7);
    }

    private Claims validateTokenExpiration(String token) {
        Claims claims = extractClaims(token);
        if (Objects.nonNull(claims.getExpiration()) && claims.getExpiration().before(new Date())) {
            throw new CustomSecurityException(HttpStatus.UNAUTHORIZED,
                    (claims.getIssuer().equals(JWT_ISSUER_SYSTEM))
                            ? SYSTEM_ACCESS_TOKEN_EXPIRED
                            : String.format(ACCESS_TOKEN_EXPIRED, claims.get(USERNAME)));
        }
        return claims;
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().verifyWith((SecretKey) getKey()).build().parseSignedClaims(token).getPayload();
    }
}
