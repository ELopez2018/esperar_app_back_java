package com.example.esperar_app.config.security;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeansInjector {

    /**
     * The user repository
     */
    private final UserRepository userRepository;

    @Autowired
    public SecurityBeansInjector(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This bean is required by the OAuth2 authorization server
     * @param authenticationProvider The authentication provider that will be used to authenticate the user
     * @return The authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return authenticationProvider::authenticate;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationStrategy = new DaoAuthenticationProvider();
        authenticationStrategy.setPasswordEncoder(passwordEncoder());
        authenticationStrategy.setUserDetailsService(userDetailsService());

        return authenticationStrategy;
    }

    /**
     * Return the password encoder that will be used to encode the user's password
     * @return The password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Return the user details service that will be used to retrieve the user's details
     * @return The user details service
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                    .orElseThrow(() ->
                            new ObjectNotFoundException("User with username [" + username + "] not found"));
    }
}
