package com.based.entity.dto;

import java.io.Serializable;
import java.util.List;

public class RowDTO implements Serializable {
    private List<Object> values;

    public List<Object> getValues() {
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("RowDTO {\n");
        for (var value : values)
            sb.append("  \"" + value + "\"\n");
        sb.append("}");
        return sb.toString();
    }
}
