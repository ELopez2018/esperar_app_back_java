package com.example.esperar_app.service.auth.impl;

import com.example.esperar_app.persistence.entity.security.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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

    private static final Logger logger = LogManager.getLogger();

    /**
     * Generate a new JWT token
     * @param userDetails is the user that will be used to generate the token
     * @param extraClaims are the extra claims that will be added to the token
     * @return the generated token
     */
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        logger.info("Generating token for user: " + userDetails.getUsername());

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
        logger.info("Generating key for JWT token");
        byte[] secretKeyDecoded = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(secretKeyDecoded);
    }

    /**
     * Extract the username from the JWT token
     * @param accessToken is the token from which the username will be extracted
     * @return the username
     */
    public String extractUsername(String accessToken) {
        logger.info("Extracting username from token");
        return extractAllClaims(accessToken).getSubject();
    }

    /**
     * Extract the extra claims from the JWT token
     * @param accessToken is the token from which the extra claims will be extracted
     * @return the extra claims
     */
    private Claims extractAllClaims(String accessToken) {
        logger.info("Extracting all claims from token");
         return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(accessToken).getPayload();
    }

    /**
     * Check if the JWT token is expired
     * @param request is the request from which the token will be extracted
     * @return token obtained from the request
     */
    public String extractJwtFromRequest(HttpServletRequest request) {
        logger.info("Extracting JWT token from request");
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith(("Bearer "))) {
            logger.warn("Token not found in request");
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
        logger.info("Extracting expiration date from token");
        return extractAllClaims(accessToken).getExpiration();
    }

    public String encryptTokenToChangePassword(User currentUser) {
        logger.info("Encrypting token to change password for user: " + currentUser.getEmail());

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date(issuedAt.getTime() + EXPIRATION_IN_MINUTES * 60 * 1000);

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .subject(currentUser.getEmail())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateTokenToChangePassword(User currentUser, String token) {
        logger.info("Validating token to change password for user: " + currentUser.getEmail());

        Claims claims = extractAllClaims(token);

        if(claims.getExpiration().before(new Date())) return false;

        return claims.getSubject().equals(currentUser.getEmail());
    }
}
