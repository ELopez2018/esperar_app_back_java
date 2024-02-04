package com.example.esperar_app.chat.controller;

import com.example.esperar_app.chat.persistence.dto.ConnectUserDto;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class UserWebSocketController {

    private final UserService userService;

    @Autowired
    public UserWebSocketController(UserService userService) {
        this.userService = userService;
    }


    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User connectUser(@Payload ConnectUserDto connectUserDto) {
        return userService.connectUser(connectUserDto.getUsername());
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(@Payload ConnectUserDto connectUserDto) {
        return userService.disconnectUser(connectUserDto.getUsername());
    }

}
