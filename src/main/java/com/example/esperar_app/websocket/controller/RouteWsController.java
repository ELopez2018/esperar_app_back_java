package com.example.esperar_app.websocket.controller;

import com.example.esperar_app.websocket.persistence.RouteWsManager;
import com.example.esperar_app.websocket.persistence.dto.InitRouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class RouteWsController {
    private final RouteWsManager routeWsManager;

    @Autowired
    public RouteWsController(RouteWsManager routeWsManager) {
        this.routeWsManager = routeWsManager;
    }

    @MessageMapping("/init-route")
    public void initRoute(@Payload InitRouteDto initRouteDto) {
        routeWsManager.initRoute(initRouteDto);
    }
}
