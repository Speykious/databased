package com.based.services;

import java.util.List;
import java.util.function.Predicate;

import com.based.entity.dto.SelectRequestDTO;
import com.based.entity.dto.TableDTO;
import com.based.exception.MissingColumnException;
import com.based.exception.MissingTableException;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Select;
import com.based.model.Storage;
import com.based.model.Table;
import com.based.model.WhereCondition;

public class SelectService {
    public List<Row> selectAll(String tableName) throws MissingTableException, MissingColumnException {
        return selectAll(tableName, null);
    }

    public List<Row> selectAll(String tableName, List<String> columnNames)
            throws MissingTableException, MissingColumnException {
        Table table = Database.getTable(tableName);
        Storage storage = table.getStorage();
        if (columnNames == null)
            return storage.getRows();
        else
            return storage.getRows(table.getColumnIndexes(columnNames));
    }

    public List<Row> selectWhere(String tableName) throws MissingTableException {
        return Database.getTable(tableName).getStorage().getRows();
    }

    public List<Row> selectWhere(String tableName, SelectRequestDTO selectRequest)
            throws MissingTableException, MissingColumnException {
        Table table = Database.getTable(tableName);
        TableDTO tableDto = table.getDTO();
        WhereCondition where = selectRequest.getWhere();
        Select select = selectRequest.getSelect();
        List<String> columns = select.getColumns();

        Predicate<Row> tester = (Row row) -> {
            Object evaluated;
            try {
                evaluated = where.evaluate(tableDto, row);
                if (evaluated instanceof Boolean)
                    return (boolean) evaluated;
            } catch (Exception e) {
                System.err.println("An error occured while testing a row: " + e);
            }

            return false;
        };
        int[] indexes = null;
        if (columns != null)
            indexes = table.getColumnIndexes(columns);
        return Database.getTable(tableName).getStorage().filter(tester, indexes);
    }
}
