package com.garage_guru.exception;

/**
 * Java 21: Final class that extends the sealed BusinessException.
 * Thrown when attempting to create a resource that already exists.
 */
public final class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
