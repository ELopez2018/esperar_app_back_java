package com.example.esperar_app.controller.user;

import com.example.esperar_app.persistence.dto.inputs.user.CreateUserDto;
import com.example.esperar_app.persistence.dto.inputs.user.RegisteredUser;
import com.example.esperar_app.persistence.dto.inputs.user.UpdateUserDto;
import com.example.esperar_app.persistence.dto.responses.GetUser;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisteredUser> signUp(@RequestBody @Valid CreateUserDto createUserDto) {
        RegisteredUser newUser = userService.create(createUserDto);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping
    public ResponseEntity<Page<GetUser>> findAll(Pageable pageable) {
        Page<GetUser> usersPage = userService.findAll(pageable);
        return ResponseEntity.ok(usersPage != null ? usersPage : Page.empty());
    }

    @GetMapping("{id}")
    public ResponseEntity<GetUser> findById(@PathVariable Long id) {
        GetUser user = userService.findById(id);
        return ResponseEntity.of(Optional.ofNullable(user));
    }

    @PutMapping("{id}")
    public ResponseEntity<GetUser> update(@PathVariable Long id, @RequestBody @Valid UpdateUserDto updateUserDto) {
        GetUser user = userService.update(id, updateUserDto);
        return ResponseEntity.of(Optional.ofNullable(user));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicle-drivers/{id}")
    public ResponseEntity<List<User>> findVehicleDrivers(@PathVariable Long id) {
        List<User> drivers = userService.findVehicleDrivers(id);
        return ResponseEntity.ok(drivers != null ? drivers : List.of());
    }
}
