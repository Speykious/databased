package com.based.entity.dto;

import java.io.Serializable;

public class WhereDTO implements Serializable{
    private String column_name;
    private String operator;
    private String value;

    public WhereDTO(String column_name, String operator, String value){
        this.column_name = column_name;
        this.operator = operator;
        this.value = value;
    }

    public String getColumnName(){ return column_name; }
    public String getOperator(){ return operator; }
    public String getValue(){ return value; }
    
}
