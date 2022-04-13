package com.based.exception;

/**
 * Exception thrown when a table doesn't exist.
 */
public class DuplicateTableException extends Exception {
    public DuplicateTableException(String tableName) {
        super("Table '" + tableName + "' already exists.");
    }
}
