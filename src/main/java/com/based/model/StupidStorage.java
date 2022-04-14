package com.based.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class StupidStorage implements Storage {
    private List<Row> rows;

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
        // Get a copy of the internal list, but not a copy of the rows
        // Avoids modification of the internal list but not the rows
        List<Row> copy = new ArrayList<>();
        for (Row row : rows)
            copy.add(row);
        return copy;
    }
}
