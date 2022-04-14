package com.based.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.based.exception.MissingTableException;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Table;

public class InsertService {
    public void insert(String tableName, List<Object> values) throws MissingTableException {
        Table table = Database.getTable(tableName);
        table.getStorage().add(new Row(values));
    }

    public int insertCsv(String tableName, InputStream csv) throws IOException, MissingTableException {
        return insertCsv(tableName, csv, ",");
    }

    public int insertCsv(String tableName, InputStream csv, String csvValueSplitter) throws IOException, MissingTableException {
        Table table = Database.getTable(tableName);
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(csv));

        int nbRows = 0;
        String csvRow;
        while ((csvRow = csvReader.readLine()) != null) {
            Object[] values = (Object[]) csvRow.split(csvValueSplitter);
            Row row = new Row(Arrays.asList(values));
            table.getStorage().add(row);
            nbRows++;
        }
        csvReader.close();

        return nbRows;
    }
}
