package com.based.model;

import java.io.Serializable;

public class WhereCondition implements Serializable {
    private String type;
    private String value;
    private WhereCondition children;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public WhereCondition getChildren() {
        return children;
    }
    public void setChildren(WhereCondition children) {
        this.children = children;
    }

}
