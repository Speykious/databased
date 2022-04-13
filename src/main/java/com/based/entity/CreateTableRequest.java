package com.based.entity;

import java.io.Serializable;

public class CreateTableRequest implements Serializable {
    private Column[] columns;

    public Column[] getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CreateTableRequest {\n");
        for (var column : columns)
            sb.append("  " + column + "\n");
        sb.append("}");
        return sb.toString();
    }
}
