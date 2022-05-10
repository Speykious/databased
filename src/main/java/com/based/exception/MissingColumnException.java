package com.based.exception;

/**
 * Exception thrown when a table doesn't exist.
 */
public class MissingColumnException extends Exception {
    public MissingColumnException(String columnName) {
        super("Column '" + columnName + "' doesn't exist.");
    }
}
