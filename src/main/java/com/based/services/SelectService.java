package com.based.services;

import java.util.List;

import com.based.exception.MissingTableException;
import com.based.model.Database;
import com.based.model.Row;

public class SelectService {
    public List<Row> selectAll(String tableName) throws MissingTableException {
        return Database.getTable(tableName).getStorage().getRows();
    }
}
