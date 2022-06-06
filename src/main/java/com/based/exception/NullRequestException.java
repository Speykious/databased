package com.based.exception;

/**
 * Exception thrown when a request doesn't exist.
 */
public class NullRequestException extends Exception {
    public NullRequestException(String message) {
        super("Invalid request format: " + message);
    }
}
