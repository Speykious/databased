package com.based.model;

import java.io.Serializable;

public class Aggregate implements Serializable {
    private String function;
    private String columnTarget;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getColumnTarget() {
        return columnTarget;
    }

    public void setColumns_target(String columnTarget) {
        this.columnTarget = columnTarget;
    }
}
