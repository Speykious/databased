package com.based.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.based.entity.TableInfo;

/**
 * Classe de stockage in-memory
 */
public final class Database {
    private static final Map<String, TableInfo> tables = new HashMap<>();
    private static final Map<String, List<List<String>>> tableValues = new HashMap<>();

    // Could send Error?
    public static TableInfo getTableInfo(String tableName) {
        return tables.get(tableName);
    }

    /**
     * Add a Table to the database
     * 
     * @param table
     */
    public static void createTable(TableInfo table) throws IllegalArgumentException {
        if (table.getName() == null)
            throw new IllegalArgumentException("Name of the table cannot be null");
        if (tables.containsKey(table.getName()))
            throw new IllegalArgumentException("Table '" + table.getName() + "' already exists.");

        tables.put(table.getName(), table);
        tableValues.put(table.getName(), new ArrayList<>());
    }

    /**
     * Select all values of a TableMetadata
     * 
     * @param tableName
     * @return
     */
    public static List<List<String>> select(String tableName) {
        // TODO: add way to specify "where" etc.
        List<List<String>> lines = tableValues.get(tableName);
        if (lines == null)
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist.");

        return lines;
    }

    private static void assertNumValues(String tableName, List<String> values) throws IllegalArgumentException {
        TableInfo tableInfo = getTableInfo(tableName);
        if (tableInfo.getColumns().size() != values.size()) {
            throw new IllegalArgumentException(
                    "Values not allowed. '" + tableName + "' should have values : "
                            + tableInfo.getColumnTypeInfo());
        }
    }

    /**
     * Update values of the database's TableMetadata
     * 
     * @param tableName
     * @param values
     */
    public static void update(String tableName, List<String> values) throws IllegalArgumentException {
        TableInfo tableInfo = getTableInfo(tableName);
        if (tableInfo == null)
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist.");

        List<List<String>> tableData = tableValues.get(tableName);
        assertNumValues(tableName, values);
        if (!tableData.fil(values.get(0)))
            throw new IllegalArgumentException(
                    "Primary key '" + values.get(0) + "' doesn't exist in table '" + tableName + "'.");

        tableData.put(values.get(0), values);

    }

    /**
     * 
     * @param tableName
     * @param values
     */
    public static void insert(String tableName, List<String> values) throws IllegalArgumentException {
        TableInfo tableInfo = getTableInfo(tableName);
        if (tableInfo == null)
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist.");

        List<List<String>> tableData = tableValues.get(tableName);
        assertNumValues(tableName, values);
        if (tableData.containsKey(values.get(0)))
            throw new IllegalArgumentException(
                    "Primary key '" + values.get(0) + "' already exists in table '" + tableName + "'.");

        tableData.put(values.get(0), values);
    }
}
