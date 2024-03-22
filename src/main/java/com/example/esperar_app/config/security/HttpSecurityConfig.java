package com.example.esperar_app.config.security;

import com.example.esperar_app.config.security.filter.JwtAuthenticationFilter;
import com.example.esperar_app.config.security.handler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class  HttpSecurityConfig {

    private final AuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public HttpSecurityConfig(
            AuthenticationProvider daoAuthenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    /**
     * This method is used to configure the HttpSecurity
     * @param httpSecurity is the object that is used to configure the HttpSecurity
     * @return SecurityFilterChain is the object that is used to configure the HttpSecurity
     * @throws Exception is the exception that is thrown when the method is called
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionMangConfig ->
                        sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(this::buildRequestMatchers)
                .exceptionHandling(exceptionHandlingConfig -> {
                    exceptionHandlingConfig.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionHandlingConfig.accessDeniedHandler(customAccessDeniedHandler);
                })
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("http://localhost:5173");
        allowedOrigins.add("http://localhost:5173/**");
        allowedOrigins.add("http://localhost:5173/src/pages/chat.html");

        configuration.setAllowedOrigins(allowedOrigins);

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        source.registerCorsConfiguration("**", configuration);

        return source;
    }


    private void buildRequestMatchers(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        // PUBLIC ENDPOINTS AUTHORIZATION
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/req-change-password").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/change-password/**").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/oauth2/**").permitAll();

        // PRIVATE ENDPOINTS
        authReqConfig.requestMatchers(HttpMethod.POST, "/users/sign-up/**").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/vehicles/vehicleStatus").permitAll();

        // PRIVATE ENDPOINTS WS
        authReqConfig.requestMatchers(HttpMethod.GET, "/ws").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/ws/**").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/ws/info/**").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/ws/info").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/ws/info?").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/ws/info?**").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/users/connectedUsers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/messages/**").permitAll();

        authReqConfig.requestMatchers(HttpMethod.POST, "/files/**").permitAll();

        authReqConfig.anyRequest().authenticated();
    }

}
