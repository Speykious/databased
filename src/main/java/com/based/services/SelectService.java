package com.based.services;

import java.util.List;
import java.util.function.Predicate;


import com.based.entity.dto.SelectRequestDTO;
import com.based.entity.dto.TableDTO;
import com.based.exception.MissingColumnException;
import com.based.exception.MissingTableException;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Table;
import com.based.model.WhereCondition;

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

    public List<Row> selectWhere(String tableName, SelectRequestDTO selectRequest) throws MissingTableException, MissingColumnException {
        Table table = Database.getTable(tableName);
        TableDTO tableDto = table.getDTO();
        WhereCondition where = selectRequest.getWhere();
        List<String> columns = selectRequest.getColumns();

        Predicate<Row> tester = (Row row) -> {
            try {
                Object evaluated = where.evaluate(tableDto, row);
                if(evaluated instanceof Boolean) return (boolean) evaluated;
                return false;
            } catch (Exception e) {
                return false;
            }
        };
        if(columns == null) throw new MissingColumnException(tableName);
        int[] indexes = table.getColumnIndexes(columns);
        return Database.getTable(tableName).getStorage().filter(tester, indexes);
    }

}
