package com.based.exception;

public class InvalidSelectException extends Exception {
    public InvalidSelectException(String message) {
        super("Invalid select format: " + message);
    }
}
