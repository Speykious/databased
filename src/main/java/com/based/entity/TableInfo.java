package com.based.entity;

import java.io.Serializable;

public class TableInfo implements Serializable {
    private String name;
    private Column[] columns;

    public String getName() {
        return name;
    }

    public Column[] getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TableInfo {\n");
        for (var column : columns)
            sb.append("  " + column + "\n");
        sb.append("}");
        return sb.toString();
    }
}
