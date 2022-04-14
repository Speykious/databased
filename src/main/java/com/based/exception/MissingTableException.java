package com.based.exception;

/**
 * Exception thrown when a table doesn't exist.
 */
public class MissingTableException extends Exception {
    public MissingTableException(String tableName) {
        super("Table '" + tableName + "' doesn't exist.");
    }
}
