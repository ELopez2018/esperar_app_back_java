package com.example.esperar_app.persistence.repository.security;

import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.utils.UserChatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM com.example.esperar_app.persistence.entity.security.User u WHERE u.vehicle.id = :vehicleId")
    List<User> findVehicleDriversByVehicleId(@Param("vehicleId") Long vehicleId);

    @Query("SELECT u FROM com.example.esperar_app.persistence.entity.security.User u WHERE u.chatStatus = :status")
    List<User> findByChatStatus(UserChatStatus status);

    Optional<User> findByChangePasswordToken(String token);

    List<User> findByUsernameOrEmailOrNit(String username, String email, String nit);
}
