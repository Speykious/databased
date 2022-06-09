package com.based.entity.dto;

import java.io.Serializable;

public class WhereDTO implements Serializable {
    private String columnName;
    private String operator;
    private String value;

    public WhereDTO(String columnName, String operator, String value) {
        this.columnName = columnName;
        this.operator = operator;
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

}
