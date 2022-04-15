package com.based.model;

import java.io.Serializable;

import com.based.exception.InvalidColumnFormatException;

public class Column implements Serializable {
    private String name;
    private String typeName;
    private boolean primaryKey;
    private boolean nullable;

    public String getName() {
        return name;
    }

    public DataType getType() {
        return DataType.DATATYPE_MAP.get(typeName);
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void assertIsValid() throws InvalidColumnFormatException {
        if (name == null || name == "")
            throw new InvalidColumnFormatException("Empty names are not allowed");
        if (!DataType.DATATYPE_MAP.containsKey(typeName))
            throw new InvalidColumnFormatException("Type '" + typeName + "' doesn't exist");
    }

    @Override
    public String toString() {
        return "Column { name: \"" + name + "\", type: " + typeName + ", nullable: " + nullable + " }";
    }
}
