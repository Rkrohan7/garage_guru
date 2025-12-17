package com.garage_guru.service;

import com.garage_guru.dto.request.LubesStockRequest;
import com.garage_guru.dto.response.LubesStockResponse;

import java.util.List;

public interface LubesStockService {
    LubesStockResponse createLubesStock(LubesStockRequest request);
    LubesStockResponse getLubesStockById(Long id);
    List<LubesStockResponse> getAllLubesStocks();
    List<LubesStockResponse> getLubesStocksByGarageId(Long garageId);
    List<LubesStockResponse> getLowStockItems(Long garageId);
    LubesStockResponse updateLubesStock(Long id, LubesStockRequest request);
    void deleteLubesStock(Long id);
}
