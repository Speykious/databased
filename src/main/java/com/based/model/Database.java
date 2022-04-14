package com.based.model;

import java.util.HashMap;
import java.util.Map;

import com.based.exception.DuplicateTableException;
import com.based.exception.MissingTableException;

/**
 * Classe de stockage in-memory
 */
public final class Database {
    private static final Map<String, Table> tables = new HashMap<>();

    private static void assertIsTable(String name) throws MissingTableException {
        if (!tables.containsKey(name))
            throw new MissingTableException(name);
    }

    private static void assertNoTable(String name) throws DuplicateTableException {
        if (tables.containsKey(name))
            throw new DuplicateTableException(name);
    }

    public static Table getTable(String name) throws MissingTableException {
        assertIsTable(name);
        return tables.get(name);
    }

    public static void addTable(Table table) throws DuplicateTableException {
        String name = table.getName();
        assertNoTable(name);
        tables.put(name, table);
    }

    public static void removeTable(String name) throws MissingTableException {
        assertIsTable(name);
        tables.remove(name);
    }

    public static void removeTable(Table table) throws MissingTableException {
        removeTable(table.getName());
    }
}
