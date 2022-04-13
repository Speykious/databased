package com.based.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de stockage in-memory
 */
public class Database {
    private static final Map<String, Table> tables = new HashMap<>();
    private static final Map<String, List<List<String>>> database = new HashMap<>();

    // Could send Error?
    public static Table getTable(String table) {
        return tables.get(table);
    }

    /**
     * Add a table to the database
     * 
     * @param tab
     */
    public static void createTable(Table tab) {
        if (tables.containsKey(tab.getName()))
            throw new IllegalArgumentException("Table '" + tab.getName() + "' already exists.");

        tables.put(tab.getName(), tab);
    }

    /**
     * Select all values of a table
     * 
     * @param tableName
     * @return
     */
    public static List<List<String>> select(String tableName) {
        var lines = database.get(tableName);
        if (lines == null)
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist.");

        return lines;
    }

    /**
     * Update values of the database's table
     * 
     * @param tableName
     * @param values
     */
    public static void update(String tableName, List<String> values) {
        List<List<String>> allTableValues = database.get(tableName);
        allTableValues.add(values);
        database.replace(tableName, database.get(tableName), allTableValues);
    }

    public static void insert(String tableName, List<String> values) {
        Table table = getTable(tableName);
        if (table == null)
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist.");

        if (table.getColumns().size() != values.size()) {
            throw new IllegalArgumentException(
                    "Values not allowed. " + tableName + " should have values : " + table.getColumns().toString());
        }

        database.get(tableName).add(values);
    }
}
