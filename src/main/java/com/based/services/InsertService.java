package com.based.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.based.distrib.MachineTarget;
import com.based.distrib.Nodes;
import com.based.entity.NbLinesResponse;
import com.based.exception.MissingTableException;
import com.based.model.Column;
import com.based.model.Database;
import com.based.model.Row;
import com.based.model.Table;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

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

    public int insertCsv(String tableName, InputStream csv)
            throws IllegalArgumentException, IOException, MissingTableException, InterruptedException {
        return insertCsv(Database.getTable(tableName), new BufferedReader(new InputStreamReader(csv)));
    }

    public int insertCsv(Table table, BufferedReader csvReader)
            throws IOException, MissingTableException, IllegalArgumentException, InterruptedException {
        StringBuilder builder = new StringBuilder();
        boolean reachedEOF = readChunkOfLines(csvReader, builder);

        Iterable<CSVRecord> records = CSV_FORMATTER.parse(new StringReader(builder.toString()));
        InsertRunnable insertRunnable = new InsertRunnable(table, records, this);

        int nbLines = 0;

        if (reachedEOF) {
            insertRunnable.run();
        } else {
            Thread insertOperation = new Thread(insertRunnable);
            insertOperation.start();
            try {
                int[] nodeIndexes = Nodes.randomlyOrderedNodeIndexes();
                while (reachedEOF == false) {
                    for (int nodeIndex : nodeIndexes) {
                        StringBuilder sb = new StringBuilder();
                        reachedEOF = readChunkOfLines(csvReader, sb);
                        String chunk = sb.toString();

                        MachineTarget nextMachine = Nodes.getMachineTarget(nodeIndex, "/csv/" + table.getName());
                        ResteasyWebTarget target = nextMachine.getTarget();
                        Builder request = target.request().accept(MediaType.MULTIPART_FORM_DATA);

                        MultipartFormDataOutput output = new MultipartFormDataOutput();
                        output.addFormData("file", chunk, MediaType.APPLICATION_OCTET_STREAM_TYPE);

                        Response response = request.post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA));
                        nbLines += response.readEntity(NbLinesResponse.class).getNbLines();

                        if (reachedEOF)
                            break;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // If there are no other online machines, we continue to read the CSV
                nbLines += insertCsv(table, csvReader);
            }
            insertOperation.join();
        }

        nbLines += insertRunnable.getNbRows();

        try {
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                // Do not break so that the rest of the buffer gets filled with nulls
            }

            builder.append(line);
        }

        return reachedEOF;
    }
}

class InsertRunnable implements Runnable {
    private Table table;
    private Iterable<CSVRecord> records;
    private InsertService insertService;
    private int nbRows = -1;

    public InsertRunnable(Table table, Iterable<CSVRecord> records, InsertService insertService) {
        this.table = table;
        this.records = records;
        this.insertService = insertService;
    }

    public int getNbRows() {
        return nbRows;
    }

    @Override
    public void run() {
        nbRows = -1;
        for (CSVRecord record : records) {
            if (nbRows == -1) {
                nbRows++;
                continue;
            }

            Row row = new Row(insertService.parseValues(table, record.toList()));
            table.getStorage().add(row);
            nbRows++;
        }
    }
}