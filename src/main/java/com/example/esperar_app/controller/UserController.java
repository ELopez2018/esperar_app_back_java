package com.example.esperar_app.controller;

import com.example.esperar_app.dto.inputs.CreateUserDto;
import com.example.esperar_app.dto.inputs.RegisteredUser;
import com.example.esperar_app.dto.responses.GetUser;
import com.example.esperar_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        if(usersPage.hasContent()) return ResponseEntity.ok(usersPage);
        else return ResponseEntity.notFound().build();
    }
}
