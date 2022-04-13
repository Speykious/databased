package com.based.model;

import java.io.Serializable;

public class Column implements Serializable {
    private String name;
    private String type;
    private boolean primaryKey;
    private boolean nullable;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isNullable() {
        return nullable;
    }

    @Override
    public String toString() {
        return "Column { name: \"" + name + "\", type: " + type + ", nullable: " + nullable + " }";
    }
}
