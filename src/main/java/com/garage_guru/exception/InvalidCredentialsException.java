package com.garage_guru.exception;

/**
 * Java 21: Final class that extends the sealed BusinessException.
 * Thrown when authentication credentials are invalid.
 */
public final class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
