package com.based.entity;

import java.io.Serializable;

public class Column implements Serializable {
    private String name;
    private String type;
    private boolean nullable;

    public String getType(){
        return type;
    }

    @Override
    public String toString() {
        return "Column { name: \"" + name + "\", type: " + type + ", nullable: " + nullable + " }";
    }
}
