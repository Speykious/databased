package com.based.entity;

import java.io.Serializable;

public class InsertRequest implements Serializable {
    private String[] values;

    public String[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("InsertRequest {\n");
        for (var value : values)
            sb.append("  \"" + value + "\"\n");
        sb.append("}");
        return sb.toString();
    }
}
