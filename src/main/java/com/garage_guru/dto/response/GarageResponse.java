package com.garage_guru.dto.response;

import java.util.List;

/**
 * Java 21 Record for Garage Response - immutable DTO
 */
public record GarageResponse(
    Long id,
    String name,
    String logoUrl,
    String address,
    String phoneNumber,
    String email,
    String googleMapLink,
    List<OwnerResponse> owners
) {}
