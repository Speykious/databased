package com.based.exception;

/**
 * Exception thrown when a table doesn't exist.
 */
public class InvalidTableFormatException extends Exception {
    public InvalidTableFormatException(String message) {
        super("Invalid table format: " + message);
    }
}
