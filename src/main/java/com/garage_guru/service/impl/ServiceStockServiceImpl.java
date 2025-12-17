package com.garage_guru.service.impl;

import com.garage_guru.dto.request.ServiceStockRequest;
import com.garage_guru.dto.response.ServiceStockResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.entity.ServiceStock;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.ServiceStockRepository;
import com.garage_guru.service.ServiceStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceStockServiceImpl implements ServiceStockService {

    private final ServiceStockRepository serviceStockRepository;
    private final GarageRepository garageRepository;

    @Override
    @Transactional
    public ServiceStockResponse createServiceStock(ServiceStockRequest request) {
        // Java 21: Using record accessor methods
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        ServiceStock stock = ServiceStock.builder()
                .name(request.name())
                .price(request.price())
                .garage(garage)
                .build();

        stock = serviceStockRepository.save(stock);
        return mapToResponse(stock);
    }

    @Override
    public ServiceStockResponse getServiceStockById(Long id) {
        ServiceStock stock = serviceStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service stock not found with id: " + id));
        return mapToResponse(stock);
    }

    @Override
    public List<ServiceStockResponse> getAllServiceStocks() {
        // Java 21: Using toList()
        return serviceStockRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ServiceStockResponse> getServiceStocksByGarageId(Long garageId) {
        // Java 21: Using toList()
        return serviceStockRepository.findByGarageId(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public ServiceStockResponse updateServiceStock(Long id, ServiceStockRequest request) {
        ServiceStock stock = serviceStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service stock not found with id: " + id));

        // Java 21: Using record accessor methods
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        stock.setName(request.name());
        stock.setPrice(request.price());
        stock.setGarage(garage);

        stock = serviceStockRepository.save(stock);
        return mapToResponse(stock);
    }

    @Override
    @Transactional
    public void deleteServiceStock(Long id) {
        if (!serviceStockRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service stock not found with id: " + id);
        }
        serviceStockRepository.deleteById(id);
    }

    private ServiceStockResponse mapToResponse(ServiceStock stock) {
        // Java 21: Using record constructor
        return new ServiceStockResponse(
                stock.getId(),
                stock.getName(),
                stock.getPrice(),
                stock.getGarage().getId(),
                stock.getGarage().getName()
        );
    }
}
