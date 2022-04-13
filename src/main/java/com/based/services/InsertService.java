package com.based.services;

import java.io.InputStream;
import java.util.List;

import com.based.exception.MissingTableException;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Table;

public class InsertService {
    public void insert(String tableName, List<Object> values) throws MissingTableException {
        Table table = Database.getTable(tableName);
        table.getStorage().add(new Row(values));
    }

    public void insertCsv(String tableName, InputStream csv) {

    }
}
