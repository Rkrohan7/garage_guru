package com.garage_guru.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Java 21 Record for Garage Request - immutable DTO
 */
public record GarageRequest(
    @NotBlank(message = "Garage name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    String logoUrl,

    String address,

    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    String phoneNumber,

    @Email(message = "Please provide a valid email address")
    String email,

    String googleMapLink
) {}
