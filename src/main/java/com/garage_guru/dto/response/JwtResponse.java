package com.garage_guru.dto.response;

import java.util.Set;

/**
 * Java 21 Record for JWT Response - immutable DTO
 */
public record JwtResponse(
    String token,
    String type,
    Long id,
    String name,
    String email,
    Set<String> roles,
    Long garageId
) {
    // Compact constructor with default type
    public JwtResponse(String token, Long id, String name, String email, Set<String> roles, Long garageId) {
        this(token, "Bearer", id, name, email, roles, garageId);
    }
}
