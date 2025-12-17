package com.garage_guru.controller;

import com.garage_guru.dto.request.LubesStockRequest;
import com.garage_guru.dto.response.LubesStockResponse;
import com.garage_guru.dto.response.MessageResponse;
import com.garage_guru.service.LubesStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lubes")
@RequiredArgsConstructor
public class LubesStockController {

    private final LubesStockService lubesStockService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LubesStockResponse> createLubesStock(@Valid @RequestBody LubesStockRequest request) {
        LubesStockResponse response = lubesStockService.createLubesStock(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LubesStockResponse> getLubesStockById(@PathVariable Long id) {
        LubesStockResponse response = lubesStockService.getLubesStockById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LubesStockResponse>> getAllLubesStocks() {
        List<LubesStockResponse> response = lubesStockService.getAllLubesStocks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<LubesStockResponse>> getLubesStocksByGarageId(@PathVariable Long garageId) {
        List<LubesStockResponse> response = lubesStockService.getLubesStocksByGarageId(garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/garage/{garageId}/low-stock")
    public ResponseEntity<List<LubesStockResponse>> getLowStockItems(@PathVariable Long garageId) {
        List<LubesStockResponse> response = lubesStockService.getLowStockItems(garageId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LubesStockResponse> updateLubesStock(@PathVariable Long id, @Valid @RequestBody LubesStockRequest request) {
        LubesStockResponse response = lubesStockService.updateLubesStock(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteLubesStock(@PathVariable Long id) {
        lubesStockService.deleteLubesStock(id);
        return ResponseEntity.ok(MessageResponse.success("Lubes stock deleted successfully"));
    }
}
