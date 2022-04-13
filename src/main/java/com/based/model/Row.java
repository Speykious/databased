package com.based.model;

import java.util.List;

public class Row {
    private int index;
    private List<Object> values;

    public int getIndex() {
        return index;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public void setValue(int index, Object value) {
        values.set(index, value);
    }
}
