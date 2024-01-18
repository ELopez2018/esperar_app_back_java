package com.example.esperar_app.controller;

import com.example.esperar_app.persistence.dto.inputs.route.CreateRouteDto;
import com.example.esperar_app.persistence.dto.inputs.route.UpdateRouteDto;
import com.example.esperar_app.persistence.entity.Route;
import com.example.esperar_app.service.route.RouteService;
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

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public ResponseEntity<Route> create(@RequestBody @Valid CreateRouteDto createRouteDto) {
        Route route = routeService.create(createRouteDto);
        if(route != null) return ResponseEntity.ok(route);
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<Page<Route>> findAll(Pageable pageable) {
        Page<Route> routesPage = routeService.findAll(pageable);
        if(routesPage.hasContent()) return ResponseEntity.ok(routesPage);
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Route> findById(@PathVariable Long id) {
        Route route = routeService.findById(id);
        if(route != null) return ResponseEntity.ok(route);
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Route> update (@PathVariable Long id, @RequestBody @Valid UpdateRouteDto updateRouteDto) {
        Route route = routeService.update(id, updateRouteDto);
        if(route != null) return ResponseEntity.ok(route);
        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
