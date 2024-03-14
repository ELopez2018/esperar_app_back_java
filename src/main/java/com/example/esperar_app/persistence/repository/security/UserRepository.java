package com.example.esperar_app.persistence.repository.security;

import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.utils.UserChatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    List<User> findByUsernameOrEmailOrNit(String username, String email, String nit);

    @Query("SELECT u FROM com.example.esperar_app.persistence.entity.security.User u " +
            "WHERE u.role.name = 'DRIVER' " +
            "AND MONTH(u.licenseExpirationDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(u.licenseExpirationDate) = YEAR(CURRENT_DATE)")
    Page<User> findDriversWithLicenseSoonToExpire(Pageable pageable);

    Optional<User> findByChangePasswordToken(String token);

    @Query("SELECT u FROM com.example.esperar_app.persistence.entity.security.User u " +
            "WHERE u.company.id = :companyId AND u.role.name = 'DRIVER'")
    Page<User> findByCompanyId(Long companyId, Pageable pageable);
}
