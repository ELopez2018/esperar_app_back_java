package com.example.esperar_app.service.user;

import com.example.esperar_app.persistence.dto.inputs.user.UpdateUserDto;
import com.example.esperar_app.exception.InvalidPasswordException;
import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.UserMapper;
import com.example.esperar_app.persistence.dto.user.CreateUserDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.user.RegisteredUser;
import com.example.esperar_app.persistence.entity.security.Role;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.entity.security.UserAuth;
import com.example.esperar_app.persistence.repository.CompanyRepository;
import com.example.esperar_app.persistence.repository.security.UserAuthRepository;
import com.example.esperar_app.persistence.repository.security.UserRepository;
import com.example.esperar_app.persistence.utils.UserChatStatus;
import com.example.esperar_app.service.auth.RoleService;
import com.example.esperar_app.service.auth.impl.JwtService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.esperar_app.service.vehicle.VehicleServiceImpl.getStrings;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final UserAuthRepository userAuthRepository;

    private final CompanyRepository companyRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RoleService roleService,
            UserMapper userMapper,
            JwtService jwtService,
            UserAuthRepository userAuthRepository,
            CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
        this.userAuthRepository = userAuthRepository;
        this.companyRepository = companyRepository;
    }
    @Override
    public RegisteredUser create(CreateUserDto createUserDto) {
        validatePassword(createUserDto);

        User user = userMapper.createUserDtoToUser(createUserDto);

        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        Role defaultRole = roleService.findDefaultRole()
                        .orElseThrow(() -> new ObjectNotFoundException("Default role not found"));

        user.setRole(defaultRole);
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user.setFullName(userMapper.getFullName(user));

        if(createUserDto.getCompanyId() != null) {
            user.setCompany(
                    companyRepository.findById(createUserDto.getCompanyId())
                            .orElseThrow(() -> new ObjectNotFoundException("Company not found"))
            );
        }

        String accessToken = jwtService.generateToken(user, generateExtraClaims(user));

        user = userRepository.save(user);

        saveUserAuth(user, accessToken);

        RegisteredUser registeredUser = userMapper.toRegisteredUser(user);
        registeredUser.setAccessToken(accessToken);
        return registeredUser;
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<GetUserDto> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(userMapper::toGetUserDto);
    }

    @Override
    public GetUserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        return userMapper.toGetUserDto(user);
    }

    @Override
    public GetUserDto update(Long id, UpdateUserDto updateUserDto) {
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

        return userMapper.toGetUserDto(existingUser);
    }

    @Override
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        userRepository.delete(user);
    }

    @Override
    public User connectUser(String username) {
        User userFound = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        userFound.setChatStatus(UserChatStatus.ONLINE);
        return userRepository.save(userFound);
    }

    @Override
    public User disconnectUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        user.setChatStatus(UserChatStatus.OFFLINE);
        return userRepository.save(user);
    }

    @Override
    public List<GetUserDto> findConnectedUsers() {
        List<User> connectedUsers = userRepository.findByChatStatus(UserChatStatus.ONLINE);

        return userMapper.toGetUserDtos(connectedUsers);
    }

    private String updateFullName(UpdateUserDto updateUserDto) {
        return updateUserDto.getFirstName() + " " +
                (updateUserDto.getSecondName() != null ? updateUserDto.getSecondName() + " " : "") +
                updateUserDto.getLastName();
    }


    private String[] getNullPropertyNames(Object source) {
        return getStrings(source);
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
