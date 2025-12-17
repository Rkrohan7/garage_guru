package com.garage_guru.service;

import com.garage_guru.dto.request.SparePartStockRequest;
import com.garage_guru.dto.response.SparePartStockResponse;

import java.util.List;

public interface SparePartStockService {
    SparePartStockResponse createSparePartStock(SparePartStockRequest request);
    SparePartStockResponse getSparePartStockById(Long id);
    List<SparePartStockResponse> getAllSparePartStocks();
    List<SparePartStockResponse> getSparePartStocksByGarageId(Long garageId);
    List<SparePartStockResponse> getLowStockItems(Long garageId);
    SparePartStockResponse updateSparePartStock(Long id, SparePartStockRequest request);
    void deleteSparePartStock(Long id);
}
