package com.based.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class StupidStorage implements Storage {
    private List<Row> rows;

    public StupidStorage() {
        rows = new ArrayList<>();
    }

    @Override
    public List<Row> filter(Predicate<Row> predicate, int[] columns) {
        //TODO: filter(Predicate<Row> predicate, Select select)
        //il faudrait recuperer les valeurs pour les aggregates au moment du parcours pour le where
        /**
         * if(select != null) if(select.aggregates == null)
         */
        ArrayList<Row> filteredRows = new ArrayList<>();
        for (Row row : rows) {
            if (predicate.test(row)) {
                //tester les aggregates 
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
        return filteredRows;
    }

    public HashMap<String, List<Row>> groupByFilter(Predicate<Row> predicate, int[] columns, int groupby) {
        /**
         * if(select != null) if(select.aggregates == null)
         */

        HashMap<String, List<Row>> map = new HashMap<>();

        // if groupby not in columns[x] throw error or empty hash map

        for (Row row : rows) {
            if (predicate.test(row)) {
                //tester les aggregates 
                if (columns == null || columns.length == 0) {
                    List<Row> values = map.get(row.getValue(groupby));
                    if(values == null){
                        List<Row> listValue = new ArrayList<Row>();
                        listValue.add(row);
                        map.put(row.getValue(groupby).toString(), listValue);
                    }
                    else {
                        //should add to the hashmap value : usefull for count
                        values.add(row);
                    }
                } else {
                    List<Row> values = map.get(row.getValue(groupby));
                    List<Object> rearranged = new ArrayList<>();
                    for (int index : columns) {
                        rearranged.add(row.getValue(index));
                    }
                    if(values == null){
                        List<Row> listValue = new ArrayList<Row>();
                        listValue.add(new Row(rearranged));
                        map.put(row.getValue(groupby).toString(), listValue);
                    }
                    else {
                        values.add(new Row(rearranged));
                    }
                }
            }
        }

        // ArrayList<Row> returnedMap = new ArrayList<>();
        // for(Map.Entry<String,List<Row>> mEntry : map.entrySet()){
        //     returnedMap.add(mEntry.getValue().get(0));
        // }

        return map;
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
