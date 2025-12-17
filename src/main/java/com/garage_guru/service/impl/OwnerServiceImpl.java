package com.garage_guru.service.impl;

import com.garage_guru.dto.request.OwnerRequest;
import com.garage_guru.dto.response.OwnerResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.entity.Owner;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.OwnerRepository;
import com.garage_guru.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final GarageRepository garageRepository;

    @Override
    @Transactional
    public OwnerResponse createOwner(OwnerRequest request) {
        // Java 21: Using record accessor methods
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        Owner owner = Owner.builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .garage(garage)
                .build();

        owner = ownerRepository.save(owner);
        return mapToResponse(owner);
    }

    @Override
    public OwnerResponse getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + id));
        return mapToResponse(owner);
    }

    @Override
    public List<OwnerResponse> getAllOwners() {
        // Java 21: Using toList() instead of collect(Collectors.toList())
        return ownerRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<OwnerResponse> getOwnersByGarageId(Long garageId) {
        // Java 21: Using toList()
        return ownerRepository.findByGarageId(garageId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public OwnerResponse updateOwner(Long id, OwnerRequest request) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + id));

        // Java 21: Using record accessor methods
        Garage garage = garageRepository.findById(request.garageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + request.garageId()));

        owner.setName(request.name());
        owner.setPhoneNumber(request.phoneNumber());
        owner.setGarage(garage);

        owner = ownerRepository.save(owner);
        return mapToResponse(owner);
    }

    @Override
    @Transactional
    public void deleteOwner(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Owner not found with id: " + id);
        }
        ownerRepository.deleteById(id);
    }

    private OwnerResponse mapToResponse(Owner owner) {
        // Java 21: Using record constructor
        return new OwnerResponse(
                owner.getId(),
                owner.getName(),
                owner.getPhoneNumber(),
                owner.getGarage().getId(),
                owner.getGarage().getName()
        );
    }
}
