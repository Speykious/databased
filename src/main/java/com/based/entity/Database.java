package com.based.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de stockage in-memory
 */
public class Database {
    private static final Map<String, Table> tables = new HashMap<>();
    private static final Map<String, List<List<String>>> database = new HashMap<>();

    //Could send Error ??
    public static Table getTable(String table){
        return tables.get(table);
    }

    /**
     * Add a table in the tables HashMap
     * @param tab
     */
    public static void putTable(Table tab){
        if(tables.containsKey(tab.getName())){
            throw new IllegalArgumentException("Table " + tab.getName() + " already exist");
        }
        tables.put(tab.getName(), tab);
    }

    /**
     * Select all values of a table
     * @param tableName
     * @return
     */
    public static List<List<String>> selectAll(String tableName){
        if(!database.containsKey(tableName)){
            throw new IllegalArgumentException("Table " + tableName + " doesn't exist.");
        }
        return database.get(tableName);
    }

    /**
     * Create the table in the database
     * @param tableName
     * @param values
     */
    public static void createTable(String tableName, List<String> values){
        List<List<String>> tableValues = new ArrayList<>();
        tableValues.add(values);
        database.put(tableName, tableValues);
    }

    /**
     * Update values of the database's table
     * @param tableName
     * @param values
     */
    public static void updateTable(String tableName, List<String> values){
        List<List<String>> allTableValues = database.get(tableName);
        allTableValues.add(values);
        database.replace(tableName, database.get(tableName), allTableValues);
    }

    public static void insert(String tableName, List<String> values){
        if(tables.containsKey(tableName)){
            Table table = getTable(tableName);
            if(table.getColumns().size() != values.size()){
                throw new IllegalArgumentException("Values not allowed. "+ tableName + " should have values : " + table.getColumns().toString());
            }
            if(!database.containsKey(tableName)){
                createTable(tableName, values);
            }
            else{
                updateTable(tableName, values);
            }
        }
    }
}
