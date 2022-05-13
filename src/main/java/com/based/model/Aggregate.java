package com.based.model;

import java.io.Serializable;
import java.util.List;

public class Aggregate implements Serializable{
    private String function;
    private List<String> columns_target;
    
    public String getFunction() {
        return function;
    }
    public void setFunction(String function) {
        this.function = function;
    }
    public List<String> getColumns_target() {
        return columns_target;
    }
    public void setColumns_target(List<String> columns_target) {
        this.columns_target = columns_target;
    }
    
}
