package com.garage_guru.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Java 21 Record for Team Member Request - immutable DTO
 */
public record TeamMemberRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    String position,

    String address,

    @Size(max = 10, message = "PAN must not exceed 10 characters")
    String pan,

    @Email(message = "Please provide a valid email address")
    String email,

    @Size(max = 15, message = "Contact number must not exceed 15 characters")
    String contactNumber,

    LocalDate joiningDate,

    @NotNull(message = "Garage ID is required")
    Long garageId
) {}
