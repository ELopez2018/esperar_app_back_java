package com.example.esperar_app.websocket.persistence;

import com.example.esperar_app.persistence.dto.route.GetRouteDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.service.route.RouteService;
import com.example.esperar_app.service.user.UserService;
import com.example.esperar_app.websocket.persistence.dto.GeolocationResponseDto;
import com.example.esperar_app.websocket.persistence.dto.InitRouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RouteWsManager {

    private final RouteService routeService;
    private final SimpMessagingTemplate messagingTemplate;

    private final UserService userService;

    @Autowired
    public RouteWsManager(
            RouteService routeService,
            SimpMessagingTemplate messagingTemplate,
            UserService userService) {
        this.routeService = routeService;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    public void initRoute(InitRouteDto initRouteDto) {
        System.out.println("A customer has joined: " + initRouteDto.getUserId());
        System.out.println("Route selected is: " + initRouteDto.getRouteId());

        GetRouteDto route = routeService.findById(initRouteDto.getRouteId());

        GeolocationResponseDto response = new GeolocationResponseDto();
        response.setRoute(route);
        GetUserDto user = userService.findById(initRouteDto.getUserId());
        response.setVehicle(user.getCurrentVehicle());

        try {
            messagingTemplate.convertAndSend("/topic/route/" + initRouteDto.getRouteId(), response);
        } catch (Exception e) {
            throw new RuntimeException("Error sending message to websocket", e);
        }
    }
}
