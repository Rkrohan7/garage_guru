package com.garage_guru.controller;

import com.garage_guru.dto.request.GarageRequest;
import com.garage_guru.dto.response.GarageResponse;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.service.GarageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GarageResponse> createGarage(@Valid @RequestBody GarageRequest request) {
        GarageResponse response = garageService.createGarage(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageResponse> getGarageById(@PathVariable Long id) {
        GarageResponse response = garageService.getGarageById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<GarageResponse>> getAllGarages() {
        List<GarageResponse> response = garageService.getAllGarages();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GarageResponse> updateGarage(@PathVariable Long id, @Valid @RequestBody GarageRequest request) {
        GarageResponse response = garageService.updateGarage(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteGarage(@PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.ok(MessageResponse.success("Garage deleted successfully"));
    }
}
