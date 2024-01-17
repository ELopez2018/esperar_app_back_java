package com.example.esperar_app.service.impl;

import com.example.esperar_app.persistence.dto.inputs.CreateUserDto;
import com.example.esperar_app.persistence.dto.inputs.RegisteredUser;
import com.example.esperar_app.persistence.dto.inputs.UpdateUserDto;
import com.example.esperar_app.persistence.dto.responses.GetUser;
import com.example.esperar_app.exception.InvalidPasswordException;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.UserMapper;
import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.security.UserAuth;
import com.example.esperar_app.persistence.repository.security.UserAuthRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import com.example.esperar_app.service.RoleService;
import com.example.esperar_app.service.UserService;
import com.example.esperar_app.service.auth.JwtService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final UserAuthRepository userAuthRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleService roleService,
            UserMapper userMapper,
            JwtService jwtService,
            UserAuthRepository userAuthRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.userAuthRepository = userAuthRepository;
    }
    @Override
    public RegisteredUser create(CreateUserDto createUserDto) {
        validatePassword(createUserDto);

        User user = userMapper.createUserDtoToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        Role defaultRole = roleService.findDefaultRole()
                        .orElseThrow(() -> new ObjectNotFoundException("Default role not found"));

        user.setRole(defaultRole);
        user.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        user.setFullName(userMapper.getFullName(user));

        String accessToken = jwtService.generateToken(user, generateExtraClaims(user));

        user = userRepository.save(user);

        saveUserAuth(user, accessToken);

        return userMapper.toRegisteredUser(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetUser> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        List<User> users = usersPage.getContent();
        List<GetUser> dtos = users.isEmpty() ? List.of() : userMapper.toGetUsers(users);

        return new PageImpl<>(dtos, pageable, usersPage.getTotalElements());
    }

    @Override
    public GetUser findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        return userMapper.toGetUser(user);
    }

    @Override
    public GetUser update(Long id, UpdateUserDto updateUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        boolean namesChanged = !Objects.equals(existingUser.getFirstName(), updateUserDto.getFirstName()) ||
                !Objects.equals(existingUser.getSecondName(), updateUserDto.getSecondName()) ||
                !Objects.equals(existingUser.getLastName(), updateUserDto.getLastName());

        if (namesChanged) {
            String fullName = updateFullName(updateUserDto);
            existingUser.setFullName(fullName);
        }

        if(updateUserDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }

        BeanUtils.copyProperties(updateUserDto, existingUser, getNullPropertyNames(updateUserDto));

        userRepository.save(existingUser);

        return userMapper.toGetUser(existingUser);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        userRepository.delete(user);
    }

    private String updateFullName(UpdateUserDto updateUserDto) {
        return updateUserDto.getFirstName() + " " +
                (updateUserDto.getSecondName() != null ? updateUserDto.getSecondName() + " " : "") +
                updateUserDto.getLastName();
    }


    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private void validatePassword(CreateUserDto createUserDto) {
        if(!StringUtils.hasText(createUserDto.getPassword()) ||
                !StringUtils.hasText(createUserDto.getConfirmPassword())) {
            throw new InvalidPasswordException("Password and confirm password are required");
        }

        if(!createUserDto.getPassword().equals(createUserDto.getConfirmPassword())) {
            throw new InvalidPasswordException("Password and confirm password must match");
        }
    }

    /**
     * Generate extra claims for the JWT token
     * @param user is the user that will be used to generate the claims
     * @return a map with the claims
     */
    public Map<String, Object> generateExtraClaims(User user) {
        return Map.of(
                "name", user.getFullName(),
                "role", user.getRole().getName(),
                "authorities", user.getAuthorities()
        );
    }

    public void saveUserAuth(User user, String accessToken) {
        UserAuth userAuth = new UserAuth();
        userAuth.setToken(accessToken);
        userAuth.setUser(user);
        userAuth.setExpirationDate(jwtService.extractExpiration(accessToken));
        userAuth.setValid(true);

        userAuthRepository.save(userAuth);
    }
}
