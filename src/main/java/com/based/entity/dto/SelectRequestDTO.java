package com.based.entity.dto;

import java.io.Serializable;
import java.util.ArrayList;

import com.based.model.WhereCondition;

public class SelectRequestDTO implements Serializable {
    private ArrayList<String> columns;
    private WhereCondition where;
    
    public ArrayList<String> getColumns() {
        return columns;
    }
    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }
    public WhereCondition getWhere() {
        return where;
    }
    public void setWhere(WhereCondition where) {
        this.where = where;
    }
    
}
