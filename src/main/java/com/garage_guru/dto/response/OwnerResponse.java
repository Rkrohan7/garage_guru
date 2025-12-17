package com.garage_guru.dto.response;

/**
 * Java 21 Record for Owner Response - immutable DTO
 */
public record OwnerResponse(
    Long id,
    String name,
    String phoneNumber,
    Long garageId,
    String garageName
) {}
