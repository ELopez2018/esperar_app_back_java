package com.example.esperar_app.controller.route;

import com.example.esperar_app.persistence.dto.route.CreateRouteDto;
import com.example.esperar_app.persistence.dto.route.GetRouteDto;
import com.example.esperar_app.persistence.dto.route.UpdateRouteDto;
import com.example.esperar_app.service.route.RouteService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<GetRouteDto> create(@RequestBody @Valid CreateRouteDto createRouteDto) {
        logger.info("Create a new route request received.");
        GetRouteDto route = routeService.create(createRouteDto);
        if(route != null) return ResponseEntity.ok(route);
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<Page<GetRouteDto>> findAll(Pageable pageable) {
        logger.info("Find all routes request received.");
        Page<GetRouteDto> routesPage = routeService.findAll(pageable);
        return ResponseEntity.ok(routesPage != null ? routesPage : Page.empty());
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<GetRouteDto> findById(@PathVariable Long id) {
        logger.info("Find route with id: [" + id + "] request received.");
        GetRouteDto route = routeService.findById(id);
        return ResponseEntity.of(Optional.ofNullable(route));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<GetRouteDto> update (
            @PathVariable Long id,
            @RequestBody @Valid UpdateRouteDto updateRouteDto) {
        logger.info("Update route with id: [" + id + "] request received.");
        GetRouteDto route = routeService.update(id, updateRouteDto);
        return ResponseEntity.of(Optional.ofNullable(route));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Delete route with id: [" + id + "] request received.");
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
