package com.based.entity.dto;

import java.io.Serializable;
import java.util.List;

import com.based.model.WhereCondition;

public class SelectRequestDTO implements Serializable {
    private List<String> columns;
    private WhereCondition where;
    
    public List<String> getColumns() {
        return columns;
    }
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
    public WhereCondition getWhere() {
        return where;
    }
    public void setWhere(WhereCondition where) {
        this.where = where;
    }
    
}
