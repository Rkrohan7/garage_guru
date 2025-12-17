package com.garage_guru.exception;

/**
 * Java 21: Sealed class hierarchy for business exceptions.
 *
 * Sealed classes restrict which classes can extend them, providing:
 * - Better encapsulation and control over the class hierarchy
 * - Exhaustive pattern matching in switch expressions (compiler enforces all cases)
 * - Clearer API design with well-defined exception types
 */
public sealed abstract class BusinessException extends RuntimeException
        permits ResourceNotFoundException, DuplicateResourceException, InvalidCredentialsException {

    protected BusinessException(String message) {
        super(message);
    }

    protected BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
