package com.example.esperar_app.controller.user;

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

    @MessageMapping("user.addUser")
    @SendTo("/user/topic")
    public User connectUser(@Payload User user) {
        return userService.connectUser(user);
    }

    @MessageMapping("user.disconnectUser")
    @SendTo("/user/topic")
    public User disconnectUser(User user) {
        userService.disconnectUser(user.getId());
        return user;
    }

}
