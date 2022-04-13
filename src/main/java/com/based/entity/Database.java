package com.based.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe de stockage in-memory
 */
public final class Database {
    private static final Map<String, TableInfo> tables = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> database = new HashMap<>();

    // Could send Error?
    public static TableInfo getTableInfo(String tableName) {
        return tables.get(tableName);
    }

    /**
     * Add a Table to the database
     * 
     * @param table
     */
    public static void createTable(TableInfo table) {
        if (tables.containsKey(table.getName()))
            throw new IllegalArgumentException("Table '" + table.getName() + "' already exists.");

        tables.put(table.getName(), table);
        database.put(table.getName(), new HashMap<>());
    }

    /**
     * Select all values of a TableMetadata
     * 
     * @param tableName
     * @return
     */
    public static Map<String, List<String>> select(String tableName) {
        // TODO: add way to specify "where" etc.
        Map<String, List<String>> lines = database.get(tableName);
        if (lines == null)
            throw new IllegalArgumentException("Table '" + tableName + "' doesn't exist.");

        return lines;
    }

    private static void assertNumValues(String tableName, List<String> values) throws IllegalArgumentException {
        TableInfo tableInfo = getTableInfo(tableName);
        if (tableInfo.getColumns().length != values.size()) {
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

        Map<String, List<String>> tableData = database.get(tableName);

        if (!tableData.containsKey(values.get(0)))
            throw new IllegalArgumentException(
                    "Primary key '" + values.get(0) + "' doesn't exist in table '" + tableName + "'.");

        assertNumValues(tableName, values);
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

        assertNumValues(tableName, values);
        database.get(tableName).put(values.get(0), values);
    }
}
