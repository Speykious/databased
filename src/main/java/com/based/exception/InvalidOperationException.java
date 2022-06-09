package com.based.exception;

/**
 * Exception thrown when a operation can't be done.
 */
public class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super("Invalid operation format: " + message);
    }
}
