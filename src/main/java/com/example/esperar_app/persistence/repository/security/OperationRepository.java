package com.example.esperar_app.persistence.repository.security;

import com.example.esperar_app.persistence.entity.security.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    @Query("SELECT o FROM operations o where o.isPublic = true")
    List<Operation> findByIsPublic();

    Optional<Operation> findByName(String operation);
}
