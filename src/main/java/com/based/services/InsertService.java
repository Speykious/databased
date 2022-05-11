package com.based.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.based.exception.MissingTableException;
import com.based.model.Column;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Table;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class InsertService {
    public static CSVFormat CSV_FORMATTER = CSVFormat.Builder.create()
            .setDelimiter(',')
            .setQuote('"')
            .setRecordSeparator("\n")
            .setIgnoreEmptyLines(true)
            .setAllowDuplicateHeaderNames(true)
            .setTrailingDelimiter(true)
            .build();

    /**
     * Asserts that the row is compatible with the number of columns in the table.
     * 
     * @param table  The table to get the number of columns from.
     * @param values The row to get the number of values from.
     * @throws IllegalArgumentException
     */
    private void assertNbValues(Table table, List<String> values) throws IllegalArgumentException {
        int nbColumns = table.getDTO().getColumns().size();
        if (values.size() != nbColumns)
            throw new IllegalArgumentException(
                    "Received " + values.size() + " values, but there are " + nbColumns + " columns");
    }

    /**
     * Converts a CSV row into a row of values typed after the columns of the table.
     * 
     * @param table     The table to get the columns from.
     * @param csvValues The CSV row to parse.
     * @return row that can be inserted into the table.
     * @throws IllegalArgumentException
     */
    private List<Object> parseValues(Table table, List<String> csvValues) throws IllegalArgumentException {
        assertNbValues(table, csvValues);

        List<Column> columns = table.getDTO().getColumns();
        List<Object> parsedValues = new ArrayList<>();
        for (int i = 0; i < csvValues.size(); i++) {
            var column = columns.get(i);
            parsedValues.add(column.getType().parse(csvValues.get(i), column.isNullable()));
        }

        return parsedValues;
    }

    public void insert(String tableName, String csvRow)
            throws MissingTableException, IllegalArgumentException, IOException {
        Table table = Database.getTable(tableName);
        Iterable<CSVRecord> records = CSV_FORMATTER.parse(new StringReader(csvRow));

        // csvRow should only contain one row of CSV values
        // so this for loop should only execute once.
        for (CSVRecord record : records)
            table.getStorage().add(new Row(parseValues(table, record.toList())));
    }

    private static String[] csvStringBuffer = new String[100_000];

    public int insertCsv(String tableName, InputStream csv)
            throws IOException, MissingTableException, IllegalArgumentException {
        Table table = Database.getTable(tableName);
        BufferedReader csvReader = new BufferedReader(new InputStreamReader(csv));

        boolean reachedEOF = false;
        for (int i = 0; i < csvStringBuffer.length; i++) {
            String line = csvReader.readLine();

            if (null == line) {
                reachedEOF = true;
                break;
            }

            csvStringBuffer[i] = line;
        }

        if (false == reachedEOF) {
            
        }

        Iterable<CSVRecord> records = CSV_FORMATTER.parse(csvReader);

        int nbRows = -1;
        for (CSVRecord record : records) {
            if (nbRows == -1) {
                nbRows++;
                continue;
            }

            Row row = new Row(parseValues(table, record.toList()));
            table.getStorage().add(row);
            nbRows++;
        }
        csvReader.close();

        return nbRows;
    }
}
