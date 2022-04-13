package com.based.entity;

import java.io.Serializable;

public class TableInfo implements Serializable {
    private String name;
    private List<Column> columns;

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public String getColumnTypeInfo(){
        String typeInfo = "'";
        boolean first = true;
        for (Column column : columns) {
            if(first) typeInfo += column.getType();
            else typeInfo += ", " + column.getType();
        }
        typeInfo += "'";
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
}
