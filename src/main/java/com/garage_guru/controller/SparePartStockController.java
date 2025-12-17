package com.garage_guru.controller;

import com.garage_guru.dto.request.SparePartStockRequest;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.dto.response.SparePartStockResponse;
import com.garage_guru.service.SparePartStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spare-parts")
@RequiredArgsConstructor
public class SparePartStockController {

    private final SparePartStockService sparePartStockService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SparePartStockResponse> createSparePartStock(@Valid @RequestBody SparePartStockRequest request) {
        SparePartStockResponse response = sparePartStockService.createSparePartStock(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SparePartStockResponse> getSparePartStockById(@PathVariable Long id) {
        SparePartStockResponse response = sparePartStockService.getSparePartStockById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SparePartStockResponse>> getAllSparePartStocks() {
        List<SparePartStockResponse> response = sparePartStockService.getAllSparePartStocks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<SparePartStockResponse>> getSparePartStocksByGarageId(@PathVariable Long garageId) {
        List<SparePartStockResponse> response = sparePartStockService.getSparePartStocksByGarageId(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/low-stock")
    public ResponseEntity<List<SparePartStockResponse>> getLowStockItems(@PathVariable Long garageId) {
        List<SparePartStockResponse> response = sparePartStockService.getLowStockItems(garageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SparePartStockResponse> updateSparePartStock(@PathVariable Long id, @Valid @RequestBody SparePartStockRequest request) {
        SparePartStockResponse response = sparePartStockService.updateSparePartStock(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteSparePartStock(@PathVariable Long id) {
        sparePartStockService.deleteSparePartStock(id);
        return ResponseEntity.ok(MessageResponse.success("Spare part stock deleted successfully"));
    }
}
