package com.garage_guru.service.impl;

import com.garage_guru.dto.request.SparePartStockRequest;
import com.garage_guru.dto.response.SparePartStockResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.entity.SparePartStock;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.SparePartStockRepository;
import com.garage_guru.service.SparePartStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SparePartStockServiceImpl implements SparePartStockService {

    private final SparePartStockRepository sparePartStockRepository;
    private final GarageRepository garageRepository;

    @Override
    @Transactional
    public SparePartStockResponse createSparePartStock(SparePartStockRequest request) {
        // Java 21: Using record accessor methods
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        SparePartStock stock = SparePartStock.builder()
                .itemName(request.itemName())
                .manufacturer(request.manufacturer())
                .partNo(request.partNo())
                .stockQuantity(request.stockQuantity())
                .minStockAlert(request.minStockAlert())
                .purchasePrice(request.purchasePrice())
                .sellingPrice(request.sellingPrice())
                .garage(garage)
                .build();

        stock = sparePartStockRepository.save(stock);
        return mapToResponse(stock);
    }

    @Override
    public SparePartStockResponse getSparePartStockById(Long id) {
        SparePartStock stock = sparePartStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spare part stock not found with id: " + id));
        return mapToResponse(stock);
    }

    @Override
    public List<SparePartStockResponse> getAllSparePartStocks() {
        // Java 21: Using toList()
        return sparePartStockRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SparePartStockResponse> getSparePartStocksByGarageId(Long garageId) {
        // Java 21: Using toList()
        return sparePartStockRepository.findByGarageId(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SparePartStockResponse> getLowStockItems(Long garageId) {
        // Java 21: Using toList()
        return sparePartStockRepository.findByGarageId(garageId).stream()
                .filter(stock -> stock.getMinStockAlert() != null && stock.getStockQuantity() <= stock.getMinStockAlert())
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public SparePartStockResponse updateSparePartStock(Long id, SparePartStockRequest request) {
        SparePartStock stock = sparePartStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spare part stock not found with id: " + id));

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

        stock = sparePartStockRepository.save(stock);
        return mapToResponse(stock);
    }

    @Override
    @Transactional
    public void deleteSparePartStock(Long id) {
        if (!sparePartStockRepository.existsById(id)) {
            throw new ResourceNotFoundException("Spare part stock not found with id: " + id);
        }
        sparePartStockRepository.deleteById(id);
    }

    private SparePartStockResponse mapToResponse(SparePartStock stock) {
        boolean lowStock = stock.getMinStockAlert() != null && stock.getStockQuantity() <= stock.getMinStockAlert();
        // Java 21: Using record constructor
        return new SparePartStockResponse(
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
