package com.based.entity;

import java.io.Serializable;
import java.util.List;

public class InsertRequest implements Serializable {
    private List<Object> values;

    public List<Object> getValues() {
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
