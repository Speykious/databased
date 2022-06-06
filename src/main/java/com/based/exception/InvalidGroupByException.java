package com.based.exception;

public class InvalidGroupByException extends Exception {
    public InvalidGroupByException(String message) {
        super("Invalid Group by format: " + message);
    }
}
