package com.based.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.based.distrib.BroadcastedRequests;
import com.based.distrib.Nodes;
import com.based.distrib.RequestRunnable;
import com.based.distrib.SelectRequestRunnable;
import com.based.entity.dto.SelectRequestDTO;
import com.based.entity.dto.TableDTO;
import com.based.exception.InvalidGroupByException;
import com.based.exception.InvalidOperationException;
import com.based.exception.InvalidSelectException;
import com.based.exception.MissingColumnException;
import com.based.exception.MissingTableException;
import com.based.model.Aggregate;
import com.based.model.Column;
import com.based.model.DataType;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Select;
import com.based.model.Storage;
import com.based.model.Table;
import com.based.model.WhereCondition;

public class SelectService {
    public List<Row> selectAll(String tableName, List<String> columnNames, List<Aggregate> aggregates)
            throws MissingTableException, MissingColumnException, InvalidOperationException, InvalidSelectException {
        Table table = Database.getTable(tableName);
        Storage storage = table.getStorage();
        int[] indexes = table.getColumnIndexes(columnNames);
        List<Row> response = storage.getRows(indexes);

        if (aggregates != null && !aggregates.isEmpty()) {
            int count = response.size();
            List<Object> newList = new ArrayList<>();
            for (Aggregate agg : aggregates) {
                if (agg.getFunction().equals("count")) {
                    newList.add(count);
                } else if (agg.getFunction().equals("sum")) {
                    String target = agg.getColumnTarget();
                    int targetTableIndex = table.getColumnIndex(target);
                    int responseIndex = getIndexOfTableIndexes(targetTableIndex, indexes);
                    Object sum = 0;

                    for (Row row : response) {
                        List<Object> rowValues = row.getValues();
                        if (rowValues.size() > 0) {
                            if (rowValues.get(responseIndex) instanceof Integer)
                                sum = (int) sum + (int) rowValues.get(responseIndex);
                            else if (rowValues.get(responseIndex) instanceof Float)
                                sum = (float) sum + (float) rowValues.get(responseIndex);
                            else
                                throw new InvalidOperationException("Can sum only integer or float types");
                        }
                    }
                    newList.add(sum);
                }
            }
            // add only the first row to the list output
            if (count > 0) {
                for (Object o : response.get(0).getValues())
                    newList.add(o);
            }
            response.clear();
            response.add(new Row(newList));
        }
        return response;
    }

    public List<Row> select(String tableName, SelectRequestDTO selectRequest) throws InvalidSelectException,
            MissingTableException, MissingColumnException, InvalidOperationException, InvalidGroupByException,
            InterruptedException {
        Table table = Database.getTable(tableName);
        TableDTO tableDto = table.getDTO();

        Select select = selectRequest.getSelect();
        WhereCondition where = selectRequest.getWhere();
        String groupby = selectRequest.getGroupby();

        List<Aggregate> aggregates = select == null ? null : select.getAggregates();
        List<String> columns = select == null ? null : select.getColumns();

        int[] indexes = table.getColumnIndexes(columns);

        List<Row> selected;
        if (where != null) {
            if (groupby != null) {
                System.err.println("Select Where & Groupby request");
                int groupbyIndex = table.getColumnIndex(groupby);
                Map<String, List<Row>> map = Database.getTable(tableName).getStorage()
                        .groupByFilter(getWherePredicate(tableDto, where), indexes, groupbyIndex);
                selected = toRows(map, aggregates, table, indexes);
            } else {
                System.err.println("Select Where request");
                selected = Database.getTable(tableName).getStorage()
                        .filter(getWherePredicate(tableDto, where), indexes, aggregates,
                                (target, specIndexes) -> {
                                    int targetTableIndex = table.getColumnIndex(target);
                                    return getIndexOfTableIndexes(targetTableIndex, indexes);
                                });
            }
        } else {
            if (groupby != null) {
                // when only select & groupby : Select X Group by X
                System.err.println("Select & Groupby request");
                int groupbyIndex = table.getColumnIndex(groupby);

                Map<String, List<Row>> groups = Database.getTable(tableName).getStorage()
                        .groupByFilter(null, indexes, groupbyIndex);
                selected = toRows(groups, aggregates, table, indexes);
            } else {
                System.err.println("Only Select request");
                selected = selectAll(tableName, columns, aggregates);
            }
        }

        // TODO: broadcast select requests
        if (!selectRequest.isBroadcasted()) {
            selectRequest.setBroadcasted(true);

            BroadcastedRequests<SelectRequestRunnable> broadcastedRequests = RequestRunnable.broadcastRequests(
                    SelectRequestRunnable.class,
                    Nodes.getOtherMachineTargets("/data/" + table.getName()),
                    (machineTarget, i) -> new SelectRequestRunnable(machineTarget, selectRequest));

            for (var runnable : broadcastedRequests.getSuccessfulRequestRunnables())
                selected.addAll(runnable.getResponseDto());
        }

        return selected;
    }

    private Predicate<Row> getWherePredicate(TableDTO tableDto, WhereCondition where) {
        return (Row row) -> {
            try {
                Object evaluated = where.evaluate(tableDto, row);
                if (evaluated instanceof Boolean)
                    return (boolean) evaluated;
                else
                    System.err.println("WhereCondition did not evaluate to a boolean");
            } catch (Exception e) {
                System.err.println("An error occured while testing a row: " + e.getMessage());
                System.err.println("Stack trace:");
                for (var ste : e.getStackTrace())
                    System.err.println(ste);
            }

            return false;
        };
    }

    private List<Row> toRows(Map<String, List<Row>> groups, List<Aggregate> aggregates, Table table, int[] indexes)
            throws InvalidOperationException, MissingColumnException, InvalidSelectException {
        List<Row> rows = new ArrayList<>();

        for (Entry<String, List<Row>> groupEntry : groups.entrySet()) {
            List<Row> groupRows = groupEntry.getValue();

            if (aggregates != null && !aggregates.isEmpty()) {
                List<Object> aggResults = groupRows.get(0).getValues();
                for (Aggregate agg : aggregates) {
                    if (agg.getFunction().equals("count")) {
                        aggResults.add(groupRows.size());
                    } else if (agg.getFunction().equals("sum")) {
                        String columnTarget = agg.getColumnTarget();
                        int columnIndex = table.getColumnIndex(columnTarget);
                        int responseIndex = getIndexOfTableIndexes(columnIndex, indexes);
                        Column column = table.getColumn(columnIndex);

                        List<Object> terms = new ArrayList<>();

                        for (Row row : groupRows) {
                            List<Object> rowValues = row.getValues();
                            Object value = rowValues.get(responseIndex);
                            terms.add(value);
                        }

                        DataType dataType = column.getType();
                        aggResults.add(dataType.sum(terms));
                    }
                }
                rows.add(new Row(aggResults));
            } else {
                rows.add(groupRows.get(0));
            }
        }
        return rows;
    }

    /**
     * @return the index of n if it exists in the indexes array
     * @throws InvalidSelectException if n is not in the indexes array
     */
    private int getIndexOfTableIndexes(int n, int[] indexes) throws InvalidSelectException {
        for (int i = 0; i < indexes.length; i++) {
            if (n == indexes[i])
                return i;
        }

        throw new InvalidSelectException(
                "The column target of an aggregate must be mentioned in the selected columns.");
    }
}
