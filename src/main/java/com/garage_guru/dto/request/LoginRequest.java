package com.garage_guru.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Java 21 Record for Login Request - immutable DTO with built-in equals, hashCode, toString
 */
public record LoginRequest(
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    String email,

    @NotBlank(message = "Password is required")
    String password
) {}
