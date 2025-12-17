package com.garage_guru.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Java 21 Record for Signup Request - immutable DTO
 */
public record SignupRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    String password,

    // Option 1: Link to existing garage
    Long garageId,

    // Option 2: Create new garage during registration
    String garageName,
    String garageLogoUrl,
    String garageAddress,
    String garagePhoneNumber,
    @Email(message = "Please provide a valid garage email address")
    String garageEmail,
    String garageGoogleMapLink
) {}
