package com.based.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.based.exception.MissingTableException;
import com.based.model.Column;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Table;

public class InsertService {
    private void assertNbValues(Table table, List<Object> values) throws IllegalArgumentException {
        int nbColumns = table.getDTO().getColumns().size();
        if (values.size() != nbColumns)
            throw new IllegalArgumentException(
                    "Received " + values.size() + " values, but there are " + nbColumns + " columns");
    }

    private List<Object> parseValues(Table table, List<Object> values) throws IllegalArgumentException {
        assertNbValues(table, values);

        List<Column> columns = table.getDTO().getColumns();
        List<Object> parsedValues = new ArrayList<>();
        for (int i = 0; i < values.size(); i++)
            parsedValues.add(columns.get(i).getType().parse(values.get(i)));

        return parsedValues;
    }

    public void insert(String tableName, List<Object> values)
            throws MissingTableException, IllegalArgumentException {
        Table table = Database.getTable(tableName);
        table.getStorage().add(new Row(parseValues(table, values)));
    }

    public int insertCsv(String tableName, InputStream csv)
            throws IOException, MissingTableException, IllegalArgumentException {
        return insertCsv(tableName, csv, ",");
    }

    public int insertCsv(String tableName, InputStream csv, String csvValueSplitter)
            throws IOException, MissingTableException, IllegalArgumentException {
        Table table = Database.getTable(tableName);
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(csv));

        // Skip first line as it contains the column names
        csvReader.readLine();

        int nbRows = 0;
        String csvRow;
        while ((csvRow = csvReader.readLine()) != null) {
            Object[] values = (Object[]) csvRow.split(csvValueSplitter);
            Row row = new Row(parseValues(table, Arrays.asList(values)));
            table.getStorage().add(row);
            nbRows++;
        }
        csvReader.close();

        return nbRows;
    }
}
