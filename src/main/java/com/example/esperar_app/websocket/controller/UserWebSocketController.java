package com.example.esperar_app.websocket.controller;

import com.example.esperar_app.websocket.persistence.dto.ConnectUserDto;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class UserWebSocketController {

    private final UserService userService;

    private static final Logger logger = LogManager.getLogger(UserWebSocketController.class);

    @Autowired
    public UserWebSocketController(UserService userService) {
        this.userService = userService;
    }


    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User connectUser(@Payload ConnectUserDto connectUserDto) {
        logger.info("User trying to connect at websocket: " + connectUserDto.getUsername());
        return userService.connectUser(connectUserDto.getUsername());
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(@Payload ConnectUserDto connectUserDto) {
        logger.info("User trying to disconnect at websocket: " + connectUserDto.getUsername());
        return userService.disconnectUser(connectUserDto.getUsername());
    }

}
