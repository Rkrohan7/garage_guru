package com.garage_guru.service;

import com.garage_guru.dto.request.OwnerRequest;
import com.garage_guru.dto.response.OwnerResponse;

import java.util.List;

public interface OwnerService {
    OwnerResponse createOwner(OwnerRequest request);
    OwnerResponse getOwnerById(Long id);
    List<OwnerResponse> getAllOwners();
    List<OwnerResponse> getOwnersByGarageId(Long garageId);
    OwnerResponse updateOwner(Long id, OwnerRequest request);
    void deleteOwner(Long id);
}
