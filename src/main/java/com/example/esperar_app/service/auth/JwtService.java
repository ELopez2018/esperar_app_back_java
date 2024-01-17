package com.example.esperar_app.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String JWT_SECRET_KEY;

    /**
     * Generate a new JWT token
     * @param userDetails is the user that will be used to generate the token
     * @param extraClaims are the extra claims that will be added to the token
     * @return the generated token
     */
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + EXPIRATION_IN_MINUTES * 60 * 1000);

        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Generate a new key for the JWT token
     * @return the generated key
     */
    private SecretKey generateKey() {
        byte[] secretKeyDecoded = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(secretKeyDecoded);
    }

    /**
     * Extract the username from the JWT token
     * @param accessToken is the token from which the username will be extracted
     * @return the username
     */
    public String extractUsername(String accessToken) {
        return extractAllClaims(accessToken).getSubject();
    }

    /**
     * Extract the extra claims from the JWT token
     * @param accessToken is the token from which the extra claims will be extracted
     * @return the extra claims
     */
    private Claims extractAllClaims(String accessToken) {
         return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(accessToken).getPayload();
    }

    /**
     * Check if the JWT token is expired
     * @param request is the request from which the token will be extracted
     * @return true if the token is expired, false otherwise
     */
    public String extractJwtFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith(("Bearer "))) {
            return null;
        }

        return authorizationHeader.replace("Bearer ", "");
    }

    /**
     * Extract the expiration date from the JWT token
     * @param accessToken is the token from which the expiration date will be extracted
     * @return the expiration date
     */
    public Date extractExpiration(String accessToken) {
        return extractAllClaims(accessToken).getExpiration();
    }
}
