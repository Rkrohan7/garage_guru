package com.garage_guru.service;

import com.garage_guru.dto.request.ServiceStockRequest;
import com.garage_guru.dto.response.ServiceStockResponse;

import java.util.List;

public interface ServiceStockService {
    ServiceStockResponse createServiceStock(ServiceStockRequest request);
    ServiceStockResponse getServiceStockById(Long id);
    List<ServiceStockResponse> getAllServiceStocks();
    List<ServiceStockResponse> getServiceStocksByGarageId(Long garageId);
    ServiceStockResponse updateServiceStock(Long id, ServiceStockRequest request);
    void deleteServiceStock(Long id);
}
