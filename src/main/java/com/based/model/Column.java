package com.based.model;

import java.io.Serializable;

import com.based.exception.InvalidColumnFormatException;

public class Column implements Serializable {
    private String name;
    private String type;
    private boolean primaryKey;
    private boolean nullable;

    public String getName() {
        return name;
    }

    public DataType getType() {
        return DataType.DATATYPE_MAP.get(type);
    }

    public String getTypeName() {
        return type;
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
        if (!DataType.DATATYPE_MAP.containsKey(type))
            throw new InvalidColumnFormatException("Type '" + type + "' doesn't exist");
    }

    @Override
    public String toString() {
        return "Column { name: \"" + name + "\", type: " + type + ", nullable: " + nullable + " }";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (nullable ? 1231 : 1237);
        result = prime * result + (primaryKey ? 1231 : 1237);
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
}
