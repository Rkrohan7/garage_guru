package com.garage_guru.dto.response;

import java.time.LocalDate;

/**
 * Java 21 Record for Team Member Response - immutable DTO
 */
public record TeamMemberResponse(
    Long id,
    String name,
    String position,
    String address,
    String pan,
    String email,
    String contactNumber,
    LocalDate joiningDate,
    Long garageId,
    String garageName
) {}
