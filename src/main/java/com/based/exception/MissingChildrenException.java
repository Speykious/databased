package com.based.exception;

public class MissingChildrenException extends Exception {
    public MissingChildrenException(String operation) {
        super("Missing children for " + operation + " operation");
    }
}
