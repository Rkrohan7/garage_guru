package com.garage_guru.exception;

import com.garage_guru.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler using Java 21 features:
 * - Pattern matching for instanceof
 * - Switch expressions with pattern matching
 * - Sealed classes for exhaustive pattern matching
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Java 21: Using switch expression with pattern matching for sealed exception hierarchy.
     * The compiler ensures all permitted subtypes are handled (exhaustive matching).
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        // Java 21: Switch expression with pattern matching for sealed types
        HttpStatus status = switch (ex) {
            case ResourceNotFoundException e -> HttpStatus.NOT_FOUND;
            case DuplicateResourceException e -> HttpStatus.CONFLICT;
            case InvalidCredentialsException e -> HttpStatus.UNAUTHORIZED;
        };

        String errorType = switch (ex) {
            case ResourceNotFoundException e -> "Not Found";
            case DuplicateResourceException e -> "Conflict";
            case InvalidCredentialsException e -> "Unauthorized";
        };

        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", errorType,
                "message", ex.getMessage(),
                "success", false
        );

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.FORBIDDEN.value(),
                "error", "Forbidden",
                "message", "You don't have permission to access this resource",
                "success", false
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Java 21: Using pattern matching for instanceof
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), error.getDefaultMessage());
            }
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("message", "Input validation failed");
        response.put("errors", errors);
        response.put("success", false);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Internal Server Error",
                "message", "An unexpected error occurred: " + ex.getMessage(),
                "success", false
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
