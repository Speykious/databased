package com.based.model;

import java.io.Serializable;
import java.util.List;

public class Select implements Serializable {
    private List<Aggregate> aggregates;
    private List<String> columns;

    public List<Aggregate> getAggregates() {
        return aggregates;
    }

    public void setAggregates(List<Aggregate> aggregates) {
        this.aggregates = aggregates;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
    
}
