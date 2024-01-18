package com.example.esperar_app.controller;

import com.example.esperar_app.persistence.dto.inputs.coordinate.CreateCoordinateDto;
import com.example.esperar_app.persistence.dto.inputs.coordinate.UpdateCoordinateDto;
import com.example.esperar_app.persistence.entity.Coordinate;
import com.example.esperar_app.service.coordinate.CoordinateService;
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
@RequestMapping("/coordinates")
public class CoordinateController {

    private final CoordinateService coordinateService;

    @Autowired
    public CoordinateController(CoordinateService coordinateService) {
        this.coordinateService = coordinateService;
    }

    @PostMapping
    public ResponseEntity<Coordinate> create(@RequestBody @Valid CreateCoordinateDto createCoordinateDto) {
        Coordinate coordinate = coordinateService.create(createCoordinateDto);
        if(coordinate == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(coordinate);
    }

    @GetMapping
    public ResponseEntity<Page<Coordinate>> findAll(Pageable pageable) {
        Page<Coordinate> coordinates = coordinateService.findAll(pageable);
        return ResponseEntity.ok(coordinates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coordinate> findById(@PathVariable Long id) {
        Coordinate coordinate = coordinateService.findById(id);
        if(coordinate == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(coordinate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coordinate> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCoordinateDto updateCoordinateDto) {
        Coordinate coordinate = coordinateService.update(id, updateCoordinateDto);
        if(coordinate == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(coordinate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coordinateService.delete(id);
        return ResponseEntity.ok().build();
    }

}
