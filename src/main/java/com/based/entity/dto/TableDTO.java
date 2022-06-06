package com.based.entity.dto;

import java.io.Serializable;
import java.util.List;

import com.based.model.Column;

/** Data Transfer Object for Table */
public class TableDTO implements Serializable {
    private String name;
    private List<Column> columns;

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getColumnsDescription() {
        String typeInfo = "[";
        boolean first = true;

        for (Column column : columns) {
            if (first) {
                typeInfo += column.getType();
                first = false;
            } else {
                typeInfo += ", " + column.getType();
            }
        }
        typeInfo += "]";
        return typeInfo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + " {\n");
        for (Column column : columns)
            sb.append("  " + column + "\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((columns == null) ? 0 : columns.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
