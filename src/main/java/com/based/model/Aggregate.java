package com.based.model;

import java.io.Serializable;

public class Aggregate implements Serializable {
    private String function;
    private String column_target;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getColumn_target() {
        return column_target;
    }

    public void setColumns_target(String column_target) {
        this.column_target = column_target;
    }
}
