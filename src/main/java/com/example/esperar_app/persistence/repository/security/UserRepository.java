package com.example.esperar_app.persistence.repository.security;

import com.example.esperar_app.persistence.entity.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM com.example.esperar_app.persistence.entity.security.User u JOIN u.companies c WHERE c.id = :companyId")
    List<User> findDriversByCompanyId(@Param("companyId") Long companyId);

}
