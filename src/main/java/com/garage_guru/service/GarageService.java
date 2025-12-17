package com.garage_guru.service;

import com.garage_guru.dto.request.GarageRequest;
import com.garage_guru.dto.response.GarageResponse;

import java.util.List;

public interface GarageService {
    GarageResponse createGarage(GarageRequest request);
    GarageResponse getGarageById(Long id);
    List<GarageResponse> getAllGarages();
    GarageResponse updateGarage(Long id, GarageRequest request);
    void deleteGarage(Long id);
}
