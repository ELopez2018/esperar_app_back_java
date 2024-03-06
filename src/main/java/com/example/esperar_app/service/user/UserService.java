package com.example.esperar_app.service.user;

import com.example.esperar_app.persistence.dto.user.CreateLegalPersonDto;
import com.example.esperar_app.persistence.dto.user.CreateNaturalPersonDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.user.RegisteredUser;
import com.example.esperar_app.persistence.dto.user.UpdateUserDto;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.utils.ImageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    RegisteredUser create(CreateNaturalPersonDto createNaturalPersonDto);

    Optional<User> findOneByUsername(String username);

    Page<GetUserDto> findAll(Pageable pageable);

    GetUserDto findById(Long id);

    GetUserDto update(Long id, UpdateUserDto updateUserDto);

    void delete(Long id);

    User disconnectUser(String username);

    List<GetUserDto> findConnectedUsers();

    User connectUser(String username);

    RegisteredUser createLegalPerson(CreateLegalPersonDto createLegalPersonDto);

    Page<GetUserDto> findDriversWithLicenseSoonToExpire(Pageable pageable);

    Boolean uploadUserDocument(MultipartFile file, Long userId, ImageType imageType);
}
