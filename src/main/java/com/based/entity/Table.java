package com.based.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Table implements Serializable {
	private String name;
    private List<Column<Class<?>>> columns;

	public Table(String name) {
		this.name = name;
        this.columns = new ArrayList<>();
	}

    public Table(String name, List<Column<Class<?>>> columns) {
		this.name = name;
        this.columns = columns;
	}

    public String getName(){
        return name;
    }

    public List<Column<Class<?>>> getColumns(){
        return columns;
    }

    public void setColumns(List<Column<Class<?>>> columns){
        this.columns = columns;
    }

    public void addColumn(Column<Class<?>> column){
        this.columns.add(column);
    }
}
