package com.based.exception;

/**
 * Exception thrown when a table doesn't exist.
 */
public class InvalidColumnFormatException extends Exception {
    public InvalidColumnFormatException(String message) {
        super("Invalid column format: " + message);
    }
}
