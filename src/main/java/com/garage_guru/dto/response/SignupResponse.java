package com.garage_guru.dto.response;

/**
 * Java 21 Record for Signup Response - immutable DTO
 */
public record SignupResponse(
    String message,
    Long userId,
    String userName,
    String userEmail,
    Long garageId,
    String garageName
) {
    public static SignupResponse success(String message, Long userId, String userName, String userEmail, Long garageId, String garageName) {
        return new SignupResponse(message, userId, userName, userEmail, garageId, garageName);
    }

    public static SignupResponse success(String message, Long userId, String userName, String userEmail) {
        return new SignupResponse(message, userId, userName, userEmail, null, null);
    }
}