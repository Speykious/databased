package com.based.exception;

/**
 * Exception thrown when a table doesn't exist.
 */
public class InvalidTableDTOException extends Exception {
    public InvalidTableDTOException(String message) {
        super("Invalid table DTO format: " + message);
    }
}
