package com.based.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.based.distrib.BroadcastedRequests;
import com.based.distrib.InsertRequestRunnable;
import com.based.distrib.Nodes;
import com.based.distrib.RequestRunnable;
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
    protected List<Object> parseValues(Table table, List<String> csvValues) throws IllegalArgumentException {
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

    public int insertCsv(String tableName, InputStream csv, boolean skipFirstLine)
            throws IllegalArgumentException, IOException, MissingTableException, InterruptedException {
        return insertCsv(Database.getTable(tableName), new BufferedReader(new InputStreamReader(csv)), skipFirstLine);
    }

    public int insertCsv(Table table, BufferedReader csvReader, boolean skipFirstLine)
            throws IOException, MissingTableException, IllegalArgumentException, InterruptedException {
        System.out.println("Importing CSV file into " + table.getName()
                + (skipFirstLine ? " (skipping first line)" : " (not skipping first line)"));

        StringBuilder builder = new StringBuilder();
        boolean reachedEOF = readChunkOfLines(csvReader, builder);

        String built = builder.toString();
        if (built.isEmpty()) {
            System.out.println("Chunk of line is empty");
            return 0;
        }

        Iterable<CSVRecord> records = CSV_FORMATTER.parse(new StringReader(built));
        InsertRunnable insertRunnable = new InsertRunnable(table, records, this, skipFirstLine);

        int nbLines = 0;

        if (reachedEOF) {
            insertRunnable.run();
        } else {
            Thread insertOperation = new Thread(insertRunnable);
            insertOperation.start();
            try {
                int[] nodeIndexes = Nodes.randomlyOrderedNodeIndexes();
                while (reachedEOF == false) {
                    List<byte[]> byteStreams = new ArrayList<>();
                    for (int i = 0; i < nodeIndexes.length; i++) {
                        StringBuilder chunkBuilder = new StringBuilder();
                        reachedEOF = readChunkOfLines(csvReader, chunkBuilder);
                        byteStreams.add(chunkBuilder.toString().getBytes());
                        if (reachedEOF)
                            break;
                    }

                    BroadcastedRequests<InsertRequestRunnable> broadcastedRequests = RequestRunnable.broadcastRequests(
                            InsertRequestRunnable.class,
                            Nodes.getOtherOnlineMachineTargets("/csv/" + table.getName()),
                            (machineTarget, i) -> new InsertRequestRunnable(machineTarget, byteStreams.get(i)));

                    for (var runnable : broadcastedRequests.getSuccessfulRequestRunnables())
                        nbLines += runnable.getNbLines();

                    if (reachedEOF)
                        break;

                    InsertService insertService = new InsertService();
                    int insertedLines = insertService.insertCsv(table, csvReader, false);

                    if (insertedLines == 0)
                        reachedEOF = true;

                    nbLines += insertedLines;
                }
            } catch (IndexOutOfBoundsException e) {
                // If there are no other online machines, we continue to read the CSV
                InsertService insertService = new InsertService();
                nbLines += insertService.insertCsv(table, csvReader, false);
            }
            insertOperation.join();
        }

        int nbLinesFromRunnable = insertRunnable.getNbRows();
        System.out.println("Inserted " + nbLinesFromRunnable + " CSV lines");
        nbLines += nbLinesFromRunnable;

        System.out.println("Total lines: " + nbLines);
        return nbLines;
    }

    /**
     * Reads 100,000 lines from the reader.
     * 
     * @param reader The reader to read lines from.
     * @param buffer The buffer to store streams in.
     * @return Whether we reached EOF.
     * @throws IOException
     */
    private boolean readChunkOfLines(BufferedReader reader, StringBuilder builder) throws IOException {
        boolean reachedEOF = false;
        for (int i = 0; i < 100_000; i++) {
            String line = reader.readLine();

            if (null == line) {
                reachedEOF = true;
                break;
            }

            builder.append(line + "\n");
        }

        if (false == reader.ready())
            reachedEOF = true;

        return reachedEOF;
    }
}

class InsertRunnable implements Runnable {
    private Table table;
    private Iterable<CSVRecord> records;
    private InsertService insertService;
    private int nbRows;

    public InsertRunnable(Table table, Iterable<CSVRecord> records, InsertService insertService,
            boolean skipFirstLine) {
        this.table = table;
        this.records = records;
        this.insertService = insertService;
        this.nbRows = skipFirstLine ? -1 : 0;
    }

    public int getNbRows() {
        return nbRows;
    }

    @Override
    public void run() {
        for (CSVRecord record : records) {
            if (nbRows < 0) {
                nbRows++;
                continue;
            }

            if (nbRows % 10000 == 0)
                System.out.println("Row chunk insertion: " + (nbRows / 1000) + "%");

            Row row = new Row(insertService.parseValues(table, record.toList()));
            table.getStorage().add(row);
            nbRows++;
        }

        System.out.println("Row chunk insertion: 100% (complete)");
    }
}