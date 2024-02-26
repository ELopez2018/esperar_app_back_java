package com.example.esperar_app.config.security.filter;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.security.UserAuth;
import com.example.esperar_app.persistence.repository.security.UserAuthRepository;
import com.example.esperar_app.service.auth.impl.JwtService;
import com.example.esperar_app.service.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserAuthRepository userAuthRepository;

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserService userService,
            UserAuthRepository userAuthRepository) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // STEP 1. Get the token from the header
        String accessToken = jwtService.extractJwtFromRequest(request);

        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        // STEP 1.1 Obtain the token from the database
        Optional<UserAuth> userAuth = userAuthRepository.findByToken(accessToken);

        if (userAuth.isPresent()) {
            boolean isValid = validateToken(userAuth);
            if (!isValid) {
                filterChain.doFilter(request, response);
                return;
            }
        } else {
            filterChain.doFilter(request, response);
            return;
        }

        // STEP 2. Validate the token with the subject and the expiration date
        String username = jwtService.extractUsername(accessToken);

        // STEP 3. Set the authentication in the context
        User userDetails = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        UsernamePasswordAuthenticationToken authToken = new
                UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // STEP 4. Continue the filter chain
        filterChain.doFilter(request, response);
    }


    /**
     * Validate the token
     * @param userAuth user auth
     * @return true if the token is valid
     */
    private boolean validateToken(Optional<UserAuth> userAuth) {
        if(userAuth.isEmpty()) return false;

        Date now = new Date(System.currentTimeMillis());

        boolean isValid = userAuth.get().isValid() && userAuth.get().getExpirationDate().after(now);

        if(!isValid) {
            logger.warn("Token is not valid");
            updateTokenStatus(userAuth.get());
        }

        return isValid;
    }

    /**
     * Update the token status
     * @param userAuth user auth
     */
    private void updateTokenStatus(UserAuth userAuth) {
        userAuth.setValid(false);
        userAuthRepository.save(userAuth);
    }
}
