package com.based.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.based.entity.Table;

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
     * After a CreateTable request
     * @param tab
     */
    public static void putTable(Table tab){
        if(tables.containsKey(tab.getName())){
            throw new IllegalArgumentException("Table " + tab.getName() + " already exist");
        }
        tables.put(tab.getName(), tab);
    }

    public static void putTableInDB(String tableName, List<String> values){
        List<List<String>> columnValues = new ArrayList<>();
        for (String value : values) {
            List<String> newColumn = new ArrayList<>();
            newColumn.add(value);
            columnValues.add(newColumn);
        }
        database.put(tableName, columnValues);
    }

    public static void addValuesInDB(String tableName, List<String> values){
        List<List<String>> allTableValues = database.get(tableName);
        for(int i = 0; i < allTableValues.size(); i++){
            allTableValues.get(i).add(values.get(i));
        }
        database.replace(tableName, database.get(tableName), allTableValues);
    }

    public static void addToDB(String tableName, List<String> values){
        if(tables.containsKey(tableName)){
            Table table = getTable(tableName);
            if(table.getColumns().size() != values.size()){
                throw new IllegalArgumentException("Values not allowed. "+ tableName + " should have values : " + table.getColumns().toString());
            }
            if(!database.containsKey(tableName)){
                putTableInDB(tableName, values);
            }
            else{
                addValuesInDB(tableName, values);
            }
        }
    }
}
