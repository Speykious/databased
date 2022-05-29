package com.based.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import com.based.entity.dto.SelectRequestDTO;
import com.based.entity.dto.TableDTO;
import com.based.exception.MissingColumnException;
import com.based.exception.MissingTableException;
import com.based.model.Aggregate;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Select;
import com.based.model.Storage;
import com.based.model.Table;
import com.based.model.WhereCondition;

public class SelectService {

    public List<Row> selectAll(String tableName, List<String> columnNames, List<Aggregate> aggregates)
            throws MissingTableException, MissingColumnException, Exception {
        Table table = Database.getTable(tableName);
        Storage storage = table.getStorage();
        int[] indexes = table.getColumnIndexes(columnNames);
        List<Row> response = storage.getRows(indexes);

        if(aggregates != null && !aggregates.isEmpty()){
            int count = response.size();
            List<Object> newList = new ArrayList<>();
            for(Aggregate agg : aggregates){
                if(agg.getFunction().equals("count")){
                    newList.add(count);                    
                }
                else if(agg.getFunction().equals("sum")){
                    String target = agg.getColumn_target();
                    int targetTableIndexe = table.getColumnIndexe(target);
                    int responseIndexe = getIndexeOfTableIndexes(targetTableIndexe, indexes);
                    Object sum = 0;

                    for(Row row : response){
                        List<Object> rowValues = row.getValues();
                        if(rowValues.size() > 0){
                            if(rowValues.get(responseIndexe) instanceof Integer){
                                sum = (Integer) sum + (Integer) rowValues.get(responseIndexe);
                            }
                            else if(rowValues.get(responseIndexe) instanceof Float){
                                sum = (Float) sum + (Float) rowValues.get(responseIndexe);
                            }
                            else {
                                throw new Exception("Can sum only integer or float types");
                            }
                        }
                    }
                    newList.add(sum);
                }
            }
            //add only the first row to the list output
            if(count > 0){
                for(Object o : response.get(0).getValues()){
                    newList.add(o);
                }
            }
            response.clear();
            response.add(new Row(newList));
        }
        return response;
    }

    public List<Row> select(String tableName, SelectRequestDTO selectRequest) throws Exception {
        Table table = Database.getTable(tableName);
        TableDTO tableDto = table.getDTO();
        Select select = selectRequest.getSelect();
        WhereCondition where = selectRequest.getWhere();
        String groupby = selectRequest.getGroupby();
        int[] groupbyIndexes = null;
        
        if(select == null) throw new Exception("No select Exception : Must specified a select field in the request");

        List<Aggregate> aggregates = select.getAggregates();
        List<String> columns = select.getColumns();
        
        if(columns == null) throw new Exception("NoSelectedColumnsException : Must specified a columns field in the select request with a list of column_name or an empty list");
        int[] indexes = table.getColumnIndexes(columns);

        if(where != null){
            if(groupby != null){
                System.err.println("Where & Groupby request");
                groupbyIndexes = table.getColumnIndexes(List.of(groupby));
                HashMap<String, List<Row>> map = Database.getTable(tableName).getStorage().groupByFilter(getWherePredicate(tableDto, where), indexes, groupbyIndexes[0]);
                return toListOfRow(map, aggregates, table, indexes);
            }
            else {
                System.err.println("Where request");                
                return Database.getTable(tableName).getStorage().filter(getWherePredicate(tableDto, where), indexes, aggregates, (target, specIndexes) -> {
                    int targetTableIndexe = table.getColumnIndexe(target);
                    return getIndexeOfTableIndexes(targetTableIndexe, indexes);
                });
            }
        }
        else {
            if(groupby == null) {
                System.err.println("Only select request");
                return selectAll(tableName, columns, aggregates);
            }
        }
        //when only select & gb : Select X Group by X
        System.err.println("Select & Groupby request");
        groupbyIndexes = table.getColumnIndexes(List.of(groupby));

        HashMap<String, List<Row>> groupMap = Database.getTable(tableName).getStorage().groupByFilter(null,indexes, groupbyIndexes[0]);
        return toListOfRow(groupMap, aggregates, table, indexes);
    }

    private Predicate<Row> getWherePredicate(TableDTO tableDto, WhereCondition where){
        return (Row row) -> {
            try {
                Object evaluated = where.evaluate(tableDto, row);
                if (evaluated instanceof Boolean)
                    return (boolean) evaluated;
                else
                    System.err.println("An error occured while testing a row");
            } catch (Exception e) {
                System.err.println("An error occured while testing a row: " + e);
            }

            return false;
        };
    }

    private List<Row> toListOfRow(HashMap<String, List<Row>> map, List<Aggregate> aggregates, Table table, int[] indexes) throws Exception{
        ArrayList<Row> returnedMap = new ArrayList<>();
            
        for(Map.Entry<String,List<Row>> mEntry : map.entrySet()){
            List<Row> entryValues = mEntry.getValue();
            
            if(aggregates != null && !aggregates.isEmpty()){
                List<Object> o = entryValues.get(0).getValues();
                for(Aggregate agg : aggregates){
                    if(agg.getFunction().equals("count")){
                        o.add(entryValues.size());
                    }
                    else if(agg.getFunction().equals("sum")){
                        String target = agg.getColumn_target();
                        int targetTableIndexe = table.getColumnIndexe(target);
                        int responseIndexe = getIndexeOfTableIndexes(targetTableIndexe, indexes);
                        Object sum = 0;
    
                        for(Row row : entryValues){
                            List<Object> rowValues = row.getValues();
                            if(rowValues.size() > 0){
                                if(rowValues.get(responseIndexe) instanceof Integer){
                                    sum = (Integer) sum + (Integer) rowValues.get(responseIndexe);
                                }
                                else if(rowValues.get(responseIndexe) instanceof Float){
                                    sum = (Float) sum + (Float) rowValues.get(responseIndexe);
                                }
                                else {
                                    throw new Exception("Can sum only integer or float types");
                                }
                            }
                        }
                        o.add(sum);
                    }
                }
                returnedMap.add(new Row(o));
            }
            else {
                returnedMap.add(entryValues.get(0));
            }
        }
        return returnedMap;
    }

    private int getIndexeOfTableIndexes(int indexe, int[] indexes) throws Exception{
        for(int i = 0; i < indexes.length; i++){
            if(indexe == indexes[i]) return i;
        }

        throw new Exception("Column target is not mentioned in the selected columns !");
    }
}
