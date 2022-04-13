package com.based.model;

import java.util.List;

public class Row {
    private List<Object> values;

    public Row(List<Object> values) {
        this.values = values;
    }    

    public int size() {
        return values.size();
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
