package com.garage_guru.service.impl;

import com.garage_guru.dto.request.LubesStockRequest;
import com.garage_guru.dto.response.LubesStockResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.entity.LubesStock;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.LubesStockRepository;
import com.garage_guru.service.LubesStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LubesStockServiceImpl implements LubesStockService {

    private final LubesStockRepository lubesStockRepository;
    private final GarageRepository garageRepository;

    @Override
    @Transactional
    public LubesStockResponse createLubesStock(LubesStockRequest request) {
        // Java 21: Using record accessor methods
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        LubesStock stock = LubesStock.builder()
                .itemName(request.itemName())
                .manufacturer(request.manufacturer())
                .partNo(request.partNo())
                .stockQuantity(request.stockQuantity())
                .minStockAlert(request.minStockAlert())
                .purchasePrice(request.purchasePrice())
                .sellingPrice(request.sellingPrice())
                .garage(garage)
                .build();

        stock = lubesStockRepository.save(stock);
        return mapToResponse(stock);
    }

    @Override
    public LubesStockResponse getLubesStockById(Long id) {
        LubesStock stock = lubesStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lubes stock not found with id: " + id));
        return mapToResponse(stock);
    }

    @Override
    public List<LubesStockResponse> getAllLubesStocks() {
        // Java 21: Using toList()
        return lubesStockRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<LubesStockResponse> getLubesStocksByGarageId(Long garageId) {
        // Java 21: Using toList()
        return lubesStockRepository.findByGarageId(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<LubesStockResponse> getLowStockItems(Long garageId) {
        // Java 21: Using toList()
        return lubesStockRepository.findByGarageId(garageId).stream()
                .filter(stock -> stock.getMinStockAlert() != null && stock.getStockQuantity() <= stock.getMinStockAlert())
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public LubesStockResponse updateLubesStock(Long id, LubesStockRequest request) {
        LubesStock stock = lubesStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lubes stock not found with id: " + id));

        // Java 21: Using record accessor methods
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        stock.setItemName(request.itemName());
        stock.setManufacturer(request.manufacturer());
        stock.setPartNo(request.partNo());
        stock.setStockQuantity(request.stockQuantity());
        stock.setMinStockAlert(request.minStockAlert());
        stock.setPurchasePrice(request.purchasePrice());
        stock.setSellingPrice(request.sellingPrice());
        stock.setGarage(garage);

        stock = lubesStockRepository.save(stock);
        return mapToResponse(stock);
    }

    @Override
    @Transactional
    public void deleteLubesStock(Long id) {
        if (!lubesStockRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lubes stock not found with id: " + id);
        }
        lubesStockRepository.deleteById(id);
    }

    private LubesStockResponse mapToResponse(LubesStock stock) {
        boolean lowStock = stock.getMinStockAlert() != null && stock.getStockQuantity() <= stock.getMinStockAlert();
        // Java 21: Using record constructor
        return new LubesStockResponse(
                stock.getId(),
                stock.getItemName(),
                stock.getManufacturer(),
                stock.getPartNo(),
                stock.getStockQuantity(),
                stock.getMinStockAlert(),
                stock.getPurchasePrice(),
                stock.getSellingPrice(),
                stock.getGarage().getId(),
                stock.getGarage().getName(),
                lowStock
        );
    }
}
