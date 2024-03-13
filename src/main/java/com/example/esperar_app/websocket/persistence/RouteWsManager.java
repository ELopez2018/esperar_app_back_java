package com.example.esperar_app.websocket.persistence;

import com.example.esperar_app.service.route.RouteService;
import com.example.esperar_app.websocket.persistence.dto.InitRouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RouteWsManager {

    private final RouteService routeService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public RouteWsManager(RouteService routeService, SimpMessagingTemplate messagingTemplate) {
        this.routeService = routeService;
        this.messagingTemplate = messagingTemplate;
    }

    public void initRoute(InitRouteDto initRouteDto) {
        System.out.println("A customer has joined: " + initRouteDto.getUserId());
        System.out.println("Route selected is: " + initRouteDto.getRouteId());

//        routeService.initRoute(initRouteDto);

        try {
            messagingTemplate.convertAndSend("/topic/route/" + initRouteDto.getRouteId(), initRouteDto);
        } catch (Exception e) {
            throw new RuntimeException("Error sending message to websocket", e);
        }
    }
}
