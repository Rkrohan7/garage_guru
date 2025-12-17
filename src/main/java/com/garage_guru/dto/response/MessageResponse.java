package com.garage_guru.dto.response;

/**
 * Java 21 Record for Message Response - immutable DTO with factory methods
 */
public record MessageResponse(
    String message,
    boolean success
) {
    // Static factory methods for common responses
    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }

    public static MessageResponse error(String message) {
        return new MessageResponse(message, false);
    }
}
