package com.odoo.features.fuel.controller;

import com.odoo.features.fuel.dto.*;
import com.odoo.features.fuel.service.FuelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuel")
@RequiredArgsConstructor
public class FuelController {

    private final FuelService fuelService;

    @PostMapping
    public ResponseEntity<FuelResponseDTO> create(@RequestBody FuelRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fuelService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<FuelResponseDTO>> getAll() {
        return ResponseEntity.ok(fuelService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(fuelService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelResponseDTO> update(
            @PathVariable Long id,
            @RequestBody FuelRequestDTO dto) {

        return ResponseEntity.ok(fuelService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        fuelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

