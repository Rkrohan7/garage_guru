package com.garage_guru.exception;

/**
 * Java 21: Final class that extends the sealed BusinessException.
 * The 'final' modifier is required when extending a sealed class
 * (alternatives are 'sealed' or 'non-sealed').
 */
public final class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
