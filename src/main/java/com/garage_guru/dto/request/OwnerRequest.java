package com.garage_guru.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Java 21 Record for Owner Request - immutable DTO
 */
public record OwnerRequest(
    @NotBlank(message = "Owner name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    String phoneNumber,

    @NotNull(message = "Garage ID is required")
    Long garageId
) {}
