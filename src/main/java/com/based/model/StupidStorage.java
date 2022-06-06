package com.based.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.based.exception.InvalidGroupByException;
import com.based.exception.InvalidOperationException;
import com.based.exception.InvalidSelectException;
import com.based.exception.MissingColumnException;

public class StupidStorage implements Storage {
    private List<Row> rows;

    public StupidStorage() {
        rows = new ArrayList<>();
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

    @Override
    public List<Row> filter(Predicate<Row> predicate, int[] columns, List<Aggregate> aggregates, CallBackInterface getRowsCallback) throws InvalidOperationException, InvalidSelectException, MissingColumnException {
        List<Row> filteredRows = new ArrayList<>();
        for (Row row : rows) {
            if (predicate.test(row)) {
                // tester les aggregates
                if (columns == null || columns.length == 0) {
                    filteredRows.add(row);
                } else {
                    List<Object> rearranged = new ArrayList<>();
                    for (int index : columns) {
                        rearranged.add(row.getValue(index));
                    }
                    filteredRows.add(new Row(rearranged));
                }
            }
        }

        if (aggregates != null && !aggregates.isEmpty()) {
            int count = filteredRows.size();
            List<Object> newList = new ArrayList<>();
            for (Aggregate agg : aggregates) {
                if (agg.getFunction().equals("count")) {
                    newList.add(count);
                } else if (agg.getFunction().equals("sum")) {
                    String target = agg.getColumnTarget();
                    int targetIndex = getRowsCallback.getTargetIndex(target, columns);
                    Object sum = 0;

                    for (Row row : filteredRows) {
                        List<Object> rowValues = row.getValues();
                        if (rowValues.size() > 0) {
                            if (rowValues.get(targetIndex) instanceof Integer) {
                                sum = (Integer) sum + (Integer) rowValues.get(targetIndex);
                            } else if (rowValues.get(targetIndex) instanceof Float) {
                                sum = (Float) sum + (Float) rowValues.get(targetIndex);
                            }
                            else {
                                throw new InvalidOperationException("Can sum only integer or float types");
                            }
                        }
                    }
                    newList.add(sum);
                }
            }
            // add only the first row to the list output
            if (count > 0) {
                for (Object o : filteredRows.get(0).getValues()) {
                    newList.add(o);
                }
            }
            filteredRows.clear();
            filteredRows.add(new Row(newList));
        }
        return filteredRows;
    }

    /**
     * wherePredicate can be null
     */
    @Override
    public Map<String, List<Row>> groupByFilter(Predicate<Row> wherePredicate, int[] columns, int groupby) throws InvalidGroupByException {
        Map<String, List<Row>> map = new HashMap<>();

        if(!contains(columns, groupby)){
            throw new InvalidGroupByException("The column in the groupby must be specified in the select");
        }

        for (Row row : rows) {
            if (wherePredicate == null || wherePredicate.test(row)) {
                if (columns == null || columns.length == 0) {
                    List<Row> values = map.get(row.getValue(groupby));
                    if (values == null) {
                        List<Row> listValue = new ArrayList<Row>();
                        listValue.add(row);
                        map.put(row.getValue(groupby).toString(), listValue);
                    } else {
                        values.add(row);
                    }
                } else {
                    List<Row> values = map.get(row.getValue(groupby));
                    List<Object> rearranged = new ArrayList<>();
                    for (int index : columns) {
                        rearranged.add(row.getValue(index));
                    }
                    if (values == null) {
                        List<Row> listValue = new ArrayList<Row>();
                        listValue.add(new Row(rearranged));
                        map.put(row.getValue(groupby).toString(), listValue);
                    } else {
                        values.add(new Row(rearranged));
                    }
                }
            }
        }

        return map;
    }

    private boolean contains(int[] array, int number) {
        for (int i : array) {
            if (i == number)
                return true;
        }
        return false;
    }
}
