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

    public int getColumnIndex(String name) throws Exception {
        int i;
        for (i = 0; i < columns.size(); i++){
            if(columns.get(i).getName().equals(name)) return i;
        }
        throw new Exception();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + " {\n");
        for (Column column : columns)
            sb.append("  " + column + "\n");
        sb.append("}");
        return sb.toString();
    }
}
