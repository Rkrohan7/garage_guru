package com.garage_guru.controller;

import com.garage_guru.dto.request.ServiceStockRequest;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.dto.response.ServiceStockResponse;
import com.garage_guru.service.ServiceStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceStockController {

    private final ServiceStockService serviceStockService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceStockResponse> createServiceStock(@Valid @RequestBody ServiceStockRequest request) {
        ServiceStockResponse response = serviceStockService.createServiceStock(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceStockResponse> getServiceStockById(@PathVariable Long id) {
        ServiceStockResponse response = serviceStockService.getServiceStockById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceStockResponse>> getAllServiceStocks() {
        List<ServiceStockResponse> response = serviceStockService.getAllServiceStocks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<ServiceStockResponse>> getServiceStocksByGarageId(@PathVariable Long garageId) {
        List<ServiceStockResponse> response = serviceStockService.getServiceStocksByGarageId(garageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceStockResponse> updateServiceStock(@PathVariable Long id, @Valid @RequestBody ServiceStockRequest request) {
        ServiceStockResponse response = serviceStockService.updateServiceStock(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteServiceStock(@PathVariable Long id) {
        serviceStockService.deleteServiceStock(id);
        return ResponseEntity.ok(MessageResponse.success("Service stock deleted successfully"));
    }
}
