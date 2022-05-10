package com.based.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class StupidStorage implements Storage {
    private List<Row> rows;

    public StupidStorage() {
        rows = new ArrayList<>();
    }

    @Override
    public List<Row> filter(Predicate<Row> predicate) {
        ArrayList<Row> filteredRows = new ArrayList<>();
        for (Row row : rows) {
            if (predicate.test(row))
                filteredRows.add(row);
        }
        return filteredRows;
    }

    @Override
    public Row add(Row row) {
        rows.add(row);
        return row;
    }

    @Override
    public Row remove(Row row) {
        rows.remove(row);
        return row;
    }

    @Override
    public List<Row> getRows() {
        return getRows(null);
    }

    @Override
    public List<Row> getRows(int[] columns) {
        List<Row> copy = new ArrayList<>();
        if (columns == null || columns.length == 0) {
            for (Row row : rows)
                copy.add(row);
        } else {
            for (Row row : rows) {
                List<Object> rearranged = new ArrayList<>();
                for (int index : columns)
                    rearranged.add(row.getValue(index));
                copy.add(new Row(rearranged));
            }
        }
        return copy;
    }
}
