package com.example.esperar_app.controller.coordinate;

import com.example.esperar_app.persistence.dto.coordinate.CreateCoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.GetCoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.UpdateCoordinateDto;
import com.example.esperar_app.service.coordinate.CoordinateService;
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
@RequestMapping("/coordinates")
public class CoordinateController {
    private static final Logger logger = LogManager.getLogger(CoordinateController.class);

    private final CoordinateService coordinateService;

    @Autowired
    public CoordinateController(CoordinateService coordinateService) {
        this.coordinateService = coordinateService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<GetCoordinateDto> create(@RequestBody @Valid CreateCoordinateDto createCoordinateDto) {
        logger.info("Create a new coordinate request received.");
        GetCoordinateDto coordinate = coordinateService.create(createCoordinateDto);
        if(coordinate == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(coordinate);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<Page<GetCoordinateDto>> findAll(Pageable pageable) {
        logger.info("Find all coordinates request received.");
        Page<GetCoordinateDto> coordinates = coordinateService.findAll(pageable);
        return ResponseEntity.ok(coordinates != null ? coordinates : Page.empty());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<GetCoordinateDto> findById(@PathVariable Long id) {
        logger.info("Find coordinate with id: [" + id + "] request received.");
        GetCoordinateDto coordinate = coordinateService.findById(id);
        return ResponseEntity.of(Optional.ofNullable(coordinate));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<GetCoordinateDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCoordinateDto updateCoordinateDto) {
        logger.info("Update coordinate with id: [" + id + "] request received.");
        GetCoordinateDto coordinate = coordinateService.update(id, updateCoordinateDto);
        return ResponseEntity.of(Optional.ofNullable(coordinate));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Delete coordinate with id: [" + id + "] request received.");
        coordinateService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
