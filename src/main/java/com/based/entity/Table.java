package com.based.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Table implements Serializable {
	private String name;
    private List<Column> columns;

	public Table(String name) {
		this.name = name;
        this.columns = new ArrayList<>();
	}

    public String getName(){
        return name;
    }

    public List<Column> getColumns(){
        return columns;
    }

    public void setColumns(List<Column> columns){
        this.columns = columns;
    }

    public void addColumn(Column column){
        this.columns.add(column);
    }
}
