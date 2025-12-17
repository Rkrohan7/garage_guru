package com.garage_guru.service.impl;

import com.garage_guru.dto.request.GarageRequest;
import com.garage_guru.dto.response.GarageResponse;
import com.garage_guru.dto.response.OwnerResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.exception.DuplicateResourceException;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GarageServiceImpl implements GarageService {

    private final GarageRepository garageRepository;

    @Override
    @Transactional
    public GarageResponse createGarage(GarageRequest request) {
        // Java 21: Using record accessor methods
        if (request.email() != null && garageRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Garage with email already exists: " + request.email());
        }

        Garage garage = Garage.builder()
                .name(request.name())
                .logoUrl(request.logoUrl())
                .address(request.address())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .googleMapLink(request.googleMapLink())
                .build();

        garage = garageRepository.save(garage);
        return mapToResponse(garage);
    }

    @Override
    public GarageResponse getGarageById(Long id) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));
        return mapToResponse(garage);
    }

    @Override
    public List<GarageResponse> getAllGarages() {
        // Java 21: Using toList() instead of collect(Collectors.toList()) - returns unmodifiable list
        return garageRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public GarageResponse updateGarage(Long id, GarageRequest request) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));

        // Java 21: Using record accessor methods
        if (request.email() != null && !request.email().equals(garage.getEmail())) {
            if (garageRepository.existsByEmail(request.email())) {
                throw new DuplicateResourceException("Garage with email already exists: " + request.email());
            }
        }

        garage.setName(request.name());
        garage.setLogoUrl(request.logoUrl());
        garage.setAddress(request.address());
        garage.setPhoneNumber(request.phoneNumber());
        garage.setEmail(request.email());
        garage.setGoogleMapLink(request.googleMapLink());

        garage = garageRepository.save(garage);
        return mapToResponse(garage);
    }

    @Override
    @Transactional
    public void deleteGarage(Long id) {
        if (!garageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Garage not found with id: " + id);
        }
        garageRepository.deleteById(id);
    }

    private GarageResponse mapToResponse(Garage garage) {
        // Java 21: Using toList() for sequenced collections
        List<OwnerResponse> ownerResponses = garage.getOwners().stream()
                .map(owner -> new OwnerResponse(
                        owner.getId(),
                        owner.getName(),
                        owner.getPhoneNumber(),
                        garage.getId(),
                        garage.getName()
                ))
                .toList();

        // Java 21: Using record constructor
        return new GarageResponse(
                garage.getId(),
                garage.getName(),
                garage.getLogoUrl(),
                garage.getAddress(),
                garage.getPhoneNumber(),
                garage.getEmail(),
                garage.getGoogleMapLink(),
                ownerResponses
        );
    }
}
