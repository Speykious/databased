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

    //TODO : handle alone count
    public List<Row> selectAll(String tableName, List<String> columnNames)
            throws MissingTableException, MissingColumnException {
        Table table = Database.getTable(tableName);
        Storage storage = table.getStorage();
        if (columnNames == null)
            return storage.getRows();
        else
            return storage.getRows(table.getColumnIndexes(columnNames));
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
                List<String> li = new ArrayList<>(); 
                li.add(groupby);
                groupbyIndexes = table.getColumnIndexes(li);
                HashMap<String, List<Row>> map = Database.getTable(tableName).getStorage().whereGroupByFilter(getWherePredicate(tableDto, where), indexes, groupbyIndexes[0]);
                return toListOfRow(map, aggregates);
            }
            else {
                return Database.getTable(tableName).getStorage().filter(getWherePredicate(tableDto, where), indexes);
            }
        }
        else {
            if(groupby == null) {
                return selectAll(tableName, columns);
            }
        }

        //Scenario : Select X Group by X
        List<String> li = new ArrayList<>(); 
        li.add(groupby);
        groupbyIndexes = table.getColumnIndexes(li);

        HashMap<String, List<Row>> groupMap = Database.getTable(tableName).getStorage().groupByFilter(indexes, groupbyIndexes[0]);
        return toListOfRow(groupMap, aggregates);
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

    private List<Row> toListOfRow(HashMap<String, List<Row>> map, List<Aggregate> aggregates){
        ArrayList<Row> returnedMap = new ArrayList<>();
            
        for(Map.Entry<String,List<Row>> mEntry : map.entrySet()){
            List<Row> entryValues = mEntry.getValue();
            
            if(aggregates != null){
                for(Aggregate agg : aggregates){
                    if(agg.getFunction().equals("count")){
                        List<Object> o = entryValues.get(0).getValues();
                        o.add(entryValues.size());
                        returnedMap.add(new Row(o));
                    }
                    else {
                        returnedMap.add(entryValues.get(0));
                    }
                }
            }
            else {
                returnedMap.add(entryValues.get(0));
            }
        }
        return returnedMap;
    }

    // public List<Row> selectWhere(String tableName, SelectRequestDTO selectRequest) throws MissingTableException, MissingColumnException, Exception {
    //     Table table = Database.getTable(tableName);
    //     TableDTO tableDto = table.getDTO();
    //     Select select = selectRequest.getSelect();
    //     WhereCondition where = selectRequest.getWhere();
    //     String groupby = selectRequest.getGroupby();
        
    //     if(select == null) throw new Exception("No select Exception : Must specified a select field in the request");

    //     List<Aggregate> aggregates = select.getAggregates();
    //     List<String> columns = select.getColumns();
        
    //     if(columns == null) throw new Exception("NoSelectedColumnsException : Must specified a columns field in the select request with a list of column_name or an empty list");

    //     int[] groupbyIndexes = null;
    //     if(groupby != null){
    //         List<String> li = new ArrayList<>(); 
    //         li.add(groupby);
    //         groupbyIndexes = table.getColumnIndexes(li);
    //     }

    //     Predicate<Row> tester = (Row row) -> {
    //         try {
    //             Object evaluated = where.evaluate(tableDto, row);
    //             if (evaluated instanceof Boolean)
    //                 return (boolean) evaluated;
    //             else
    //                 System.err.println("An error occured while testing a row");
    //         } catch (Exception e) {
    //             System.err.println("An error occured while testing a row: " + e);
    //         }

    //         return false;
    //     };

    //     //If no select : send errors
    //     int[] indexes = table.getColumnIndexes(columns);

    //     if(groupby != null) {
    //         HashMap<String, List<Row>> map = Database.getTable(tableName).getStorage().whereGroupByFilter(tester, indexes, groupbyIndexes[0]);
    //         ArrayList<Row> returnedMap = new ArrayList<>();
            
    //         for(Map.Entry<String,List<Row>> mEntry : map.entrySet()){
    //             List<Row> entryValues = mEntry.getValue();
                
    //             if(aggregates != null){
    //                 for(Aggregate agg : aggregates){
    //                     if(agg.getFunction().equals("count")){
    //                         List<Object> o = entryValues.get(0).getValues();
    //                         o.add(entryValues.size());
    //                         returnedMap.add(new Row(o));
    //                     }
    //                     else {
    //                         returnedMap.add(entryValues.get(0));
    //                     }
    //                 }
    //             }
    //             else {
    //                 returnedMap.add(entryValues.get(0));
    //             }
    //         }
    //         return returnedMap;
    //     }
    //     return Database.getTable(tableName).getStorage().filter(tester, indexes);
    // }

    /**
         * COUNT(*) -> count number of lines : foreach(row) count++
         * COUNT(column_name) -> if(row[column_name].value != null) count++
         */

         /**
          * GROUP BY family_name
          * don't show doublon
          *
          * Select family_name , count(*)
          * From Person p
          * GROUP BY family_name
          * -> count number of occurence of family_name.value (ex: Dupond   32)
          *                                                        Cotigu   5   
          */

          //TODO :
          /**
           * For COUNT & SUM, if there is not a group by, don't handle others columns input
           * Select COUNT(*), name  X
           */
}
