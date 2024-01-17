package com.example.esperar_app.persistence.repository.security;

import com.example.esperar_app.persistence.entity.security.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByToken(String token);
}
