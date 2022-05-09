package com.based.services;

import java.util.List;

import com.based.exception.MissingTableException;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SelectService {
    public List<Row> selectAll(String tableName) throws MissingTableException, MissingColumnException {
        return selectAll(tableName, null);
    }

    public List<Row> selectAll(String tableName, List<String> columnNames)
            throws MissingTableException, MissingColumnException {
        Table table = Database.getTable(tableName);
        return table.getStorage().getRows(table.getColumnIndexes(columnNames));
    }

    public List<Row> selectWhere(String tableName) throws MissingTableException {
        return Database.getTable(tableName).getStorage().getRows();
    }

    public List<Row> selectWhere(String tableName) throws MissingTableException {
        return Database.getTable(tableName).getStorage().getRows();
    }
}
