package com.based.app;

import java.util.HashMap;
import java.util.Map;

import com.based.entity.Table;

/**
 * Classe de stockage in-memory
 */
public class Database {
    private static final Map<String, Table> tables = new HashMap<>();

    public static Table get(String table){
        return tables.get(table);
    }

    public static void put(Table tab){
        if(tables.containsKey(tab.getName())){
            throw new IllegalArgumentException("Table " + tab.getName() + " already exist");
        }
        tables.put(tab.getName(), tab);
    }
    
}
